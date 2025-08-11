package core

import (
	"fmt"
	"log"
	"os"
	"strconv"
	"strings"

	"github.com/twilio/twilio-go"
	openapi "github.com/twilio/twilio-go/rest/api/v2010"
	"gopkg.in/gomail.v2"
)

func SendSMSVerificationCode(phoneNumber, countryCode, verifyCode string) error {
	accountSid := os.Getenv("TWILIO_ACCOUNT_SID")
	authToken := os.Getenv("TWILIO_AUTH_TOKEN")
	twilioPhoneNumber := os.Getenv("TWILIO_PHONE_NUMBER")
	if accountSid == "" || authToken == "" || twilioPhoneNumber == "" {
		return fmt.Errorf("twilio credentials not configured")
	}
	client := twilio.NewRestClientWithParams(twilio.ClientParams{
		Username: accountSid,
		Password: authToken,
	})
	fullPhoneNumber := phoneNumber
	if countryCode != "" && !strings.HasPrefix(phoneNumber, "+") {
		if strings.HasPrefix(phoneNumber, "0") {
			phoneNumber = phoneNumber[1:]
		}
		fullPhoneNumber = countryCode + phoneNumber
	}
	messageBody := fmt.Sprintf("Your password reset verification code is: %s", verifyCode)
	params := &openapi.CreateMessageParams{}
	params.SetTo(fullPhoneNumber)
	params.SetFrom(twilioPhoneNumber)
	params.SetBody(messageBody)

	_, err := client.Api.CreateMessage(params)
	if err != nil {
		return fmt.Errorf("failed to send SMS: %v", err)
	}
	return nil
}

func SendEmailVerificationCode(toEmail, verifyCode string) error {
	senderEmail := os.Getenv("SEND_EMAIL")
	senderPassword := os.Getenv("EMAIL_PASSWORD")
	smtpHost := os.Getenv("SMTP_HOST")
	smtpPortStr := os.Getenv("SMTP_PORT")

	if senderEmail == "" || senderPassword == "" || smtpHost == "" || smtpPortStr == "" {
		return fmt.Errorf("email configuration not complete")
	}

	smtpPort, err := strconv.Atoi(smtpPortStr)
	if err != nil {
		return fmt.Errorf("invalid SMTP port: %v", err)
	}

	m := gomail.NewMessage()
	m.SetHeader("From", senderEmail)
	m.SetHeader("To", toEmail)
	m.SetHeader("Subject", "Password Reset Verification Code")

	body := fmt.Sprintf(`
		<html>
		<body>
			<div style="text-align: center;">
				<h1>Hera Company</h1>
			</div>
			<h2>Password Reset Request</h2>
			<p>You have requested to reset your password. Please use the following verification code:</p>
			<h3 style="color: #007bff; font-size: 24px; letter-spacing: 2px;">%s</h3>
			<p>This code will expire in 15 minutes.</p>
			<p>If you did not request this password reset, please ignore this email.</p>
		</body>
		</html>
	`, verifyCode)

	m.SetBody("text/html", body)

	d := gomail.NewDialer(smtpHost, smtpPort, senderEmail, senderPassword)

	if err := d.DialAndSend(m); err != nil {
		return fmt.Errorf("failed to send email: %v", err)
	}

	log.Printf("Email sent successfully to %s", toEmail)
	return nil
}

func SendWhatsAppVerificationCode(phoneNumber, countryCode, verifyCode string) error {
	accountSid := os.Getenv("TWILIO_ACCOUNT_SID")
	authToken := os.Getenv("TWILIO_AUTH_TOKEN")
	twilioWhatsAppNumber := os.Getenv("TWILIO_WHATSAPP_NUMBER")

	if accountSid == "" || authToken == "" || twilioWhatsAppNumber == "" {
		return fmt.Errorf("twilio WhatsApp credentials not configured")
	}

	client := twilio.NewRestClientWithParams(twilio.ClientParams{
		Username: accountSid,
		Password: authToken,
	})
	fullPhoneNumber := phoneNumber
	if countryCode != "" && !strings.HasPrefix(phoneNumber, "+") {
		if strings.HasPrefix(phoneNumber, "0") {
			phoneNumber = phoneNumber[1:]
		}
		fullPhoneNumber = countryCode + phoneNumber
	}
	fullPhoneNumber = "whatsapp:" + fullPhoneNumber

	messageBody := fmt.Sprintf("Your password reset verification code is: %s", verifyCode)

	params := &openapi.CreateMessageParams{}
	params.SetTo(fullPhoneNumber)
	params.SetFrom(twilioWhatsAppNumber)
	params.SetBody(messageBody)

	_, err := client.Api.CreateMessage(params)
	if err != nil {
		return fmt.Errorf("failed to send WhatsApp message: %v", err)
	}

	log.Printf("WhatsApp verification code sent successfully to %s", phoneNumber)
	return nil
}
