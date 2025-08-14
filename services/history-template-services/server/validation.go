package server

import (
	"regexp"
	"strings"
)

func ValidateTemplateTitle(title string) bool {
	title = strings.TrimSpace(title)
	return len(title) >= 3 && len(title) <= 200
}

func ValidateEra(era string) bool {
	validEras := []string{"Old Kingdom", "Middle Kingdom", "New Kingdom", "Late Period", "Ptolemaic Period"}
	for _, validEra := range validEras {
		if era == validEra {
			return true
		}
	}
	return false
}

func ValidateDifficulty(difficulty string) bool {
	validDifficulties := []string{"beginner", "intermediate", "advanced"}
	for _, validDiff := range validDifficulties {
		if difficulty == validDiff {
			return true
		}
	}
	return false
}

func ValidateLanguage(language string) bool {
	langPattern := regexp.MustCompile(`^[a-z]{2}(-[A-Z]{2})?$`)
	return langPattern.MatchString(language)
}

func ValidateUUID(uuid string) bool {
	uuidPattern := regexp.MustCompile(`^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$`)
	return uuidPattern.MatchString(uuid)
}
