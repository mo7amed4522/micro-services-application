package server

import (
	"database/sql"
	"fmt"
	"log"
	"os"

	_ "github.com/lib/pq"
)

type Database struct {
	db *sql.DB
}

func NewDatabase() *Database {
	host := getEnv("DB_HOST", "localhost")
	port := getEnv("DB_PORT", "5432")
	user := getEnv("DB_USER", "postgres")
	password := getEnv("DB_PASSWORD", "2521")
	dbname := getEnv("DB_NAME", "userdb")

	psqlInfo := fmt.Sprintf("host=%s port=%s user=%s password=%s dbname=%s sslmode=disable",
		host, port, user, password, dbname)

	db, err := sql.Open("postgres", psqlInfo)
	if err != nil {
		log.Fatalf("Failed to connect to database: %v", err)
	}

	if err = db.Ping(); err != nil {
		log.Fatalf("Failed to ping database: %v", err)
	}
	log.Println("Successfully connected to database")
	createTableQuery := `
	CREATE TABLE IF NOT EXISTS uploads (
		id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
		user_id VARCHAR(255) NOT NULL,
		file_key VARCHAR(500) NOT NULL UNIQUE,
		file_name VARCHAR(255) NOT NULL,
		file_type VARCHAR(50) NOT NULL,
		content_type VARCHAR(100) NOT NULL,
		file_size BIGINT NOT NULL,
		file_url TEXT NOT NULL,
		uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		deleted_at TIMESTAMP NULL
	);
	`

	if _, err := db.Exec(createTableQuery); err != nil {
		log.Fatalf("Failed to create uploads table: %v", err)
	}

	return &Database{db: db}
}

func (d *Database) Close() {
	d.db.Close()
}
func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
