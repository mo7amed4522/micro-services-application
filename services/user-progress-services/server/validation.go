package server

import (
	"regexp"
	"strings"
)

func ValidateUserID(userID string) bool {
	userID = strings.TrimSpace(userID)
	return len(userID) > 0 && len(userID) <= 100
}

func ValidateTemplateID(templateID string) bool {
	templateID = strings.TrimSpace(templateID)
	return len(templateID) > 0 && len(templateID) <= 100
}

func ValidateSectionID(sectionID string) bool {
	sectionID = strings.TrimSpace(sectionID)
	return len(sectionID) > 0 && len(sectionID) <= 100
}

func ValidateProgress(progress float32) bool {
	return progress >= 0 && progress <= 100
}

func ValidateUUID(uuid string) bool {
	uuidPattern := regexp.MustCompile(`^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$`)
	return uuidPattern.MatchString(uuid)
}

func ValidatePagination(page, limit int32) (int32, int32) {
	if page <= 0 {
		page = 1
	}
	if limit <= 0 || limit > 100 {
		limit = 10
	}
	return page, limit
}
