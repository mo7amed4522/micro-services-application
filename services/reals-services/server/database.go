package server

import (
	"fmt"
	"log"
	"os"
	"time"

	"gorm.io/driver/postgres"
	"gorm.io/gorm"
	"gorm.io/gorm/logger"
)

type Database struct {
	DB *gorm.DB
}

type Real struct {
	ID         string
	UserID     string
	MediaURLs  []string
	MediaTypes []string
	ViewCount  int
	ViewerIDs  []string
	CreatedAt  time.Time
	ExpiresAt  time.Time
	DeletedAt  *time.Time
}

func (Real) TableName() string {
	return "reals"
}

func NewDatabase() *Database {
	host := getEnv("DB_HOST", "localhost")
	port := getEnv("DB_PORT", "5432")
	user := getEnv("DB_USER", "postgres")
	password := getEnv("DB_PASSWORD", "2521")
	dbname := getEnv("DB_NAME", "userdb")

	dsn := fmt.Sprintf("host=%s user=%s password=%s dbname=%s port=%s sslmode=disable TimeZone=UTC",
		host, user, password, dbname, port)

	db, err := gorm.Open(postgres.Open(dsn), &gorm.Config{
		Logger: logger.Default.LogMode(logger.Info),
	})
	if err != nil {
		log.Fatalf("Failed to connect to database: %v", err)
	}
	if err := db.AutoMigrate(&Real{}); err != nil {
		log.Fatalf("Failed to migrate database: %v", err)
	}

	log.Println("Database connected successfully")
	return &Database{DB: db}
}
func (d *Database) GetActiveReals(limit, offset int) ([]Real, error) {
	var reals []Real
	now := time.Now()

	err := d.DB.Where("expires_at > ? AND deleted_at IS NULL", now).
		Order("created_at DESC").
		Limit(limit).
		Offset(offset).
		Find(&reals).Error

	return reals, err
}
func (d *Database) GetUserReals(userID string) ([]Real, error) {
	var reals []Real
	now := time.Now()

	err := d.DB.Where("user_id = ? AND expires_at > ? AND deleted_at IS NULL", userID, now).
		Order("created_at DESC").
		Find(&reals).Error

	return reals, err
}
func (d *Database) CreateReal(real *Real) error {
	return d.DB.Create(real).Error
}
func (d *Database) GetRealByID(realID string) (*Real, error) {
	var real Real
	err := d.DB.Where("id = ? AND deleted_at IS NULL", realID).First(&real).Error
	if err != nil {
		return nil, err
	}
	return &real, nil
}
func (d *Database) UpdateReal(real *Real) error {
	return d.DB.Save(real).Error
}
func (d *Database) SoftDeleteReal(realID string) error {
	now := time.Now()
	return d.DB.Model(&Real{}).Where("id = ?", realID).Update("deleted_at", now).Error
}
func (d *Database) CleanupExpiredReals() error {
	now := time.Now()
	return d.DB.Model(&Real{}).Where("expires_at <= ? AND deleted_at IS NULL", now).Update("deleted_at", now).Error
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
