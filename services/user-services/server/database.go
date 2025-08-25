package server

import (
	"database/sql"
	"fmt"
	"log"
	"os"
	"time"

	_ "github.com/lib/pq"
)

type Database struct {
	DB *sql.DB
}

// MARK: connect Databasee
func NewDatabase() (*Database, error) {
	host := getEnv("DB_HOST", "localhost")
	port := getEnv("DB_PORT", "5432")
	user := getEnv("DB_USER", "postgres")
	password := getEnv("DB_PASSWORD", "2521")
	dbname := getEnv("DB_NAME", "userdb")

	psqlInfo := fmt.Sprintf("host=%s port=%s user=%s password=%s dbname=%s sslmode=disable",
		host, port, user, password, dbname)
	var db *sql.DB
	var err error
	for i := 0; i < 5; i++ {
		db, err = sql.Open("postgres", psqlInfo)
		if err == nil {
			if err = db.Ping(); err == nil {
				log.Println("Successfully connected to database")
				return &Database{DB: db}, nil
			}
		}
		log.Printf("Database connection attempt %d failed: %v", i+1, err)
		time.Sleep(time.Duration(i+1) * 2 * time.Second)
	}
	return nil, fmt.Errorf("failed to connect to database after 5 attempts: %v", err)
}
func (d *Database) Close() {
	if d.DB != nil {
		d.DB.Close()
	}
}
func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
