# Feedback Services

A gRPC-based microservice for managing user feedback on templates within the Luxor application ecosystem.

## Overview

The Feedback Service provides a robust API for collecting, storing, and managing user feedback on various templates. It supports operations like submitting feedback, retrieving feedback statistics, and managing existing feedback entries.

## Features

- **Submit Feedback**: Allow users to submit ratings and comments for templates
- **Retrieve Template Feedback**: Get paginated feedback for specific templates
- **Feedback Statistics**: Calculate average ratings and total feedback counts
- **Update Feedback**: Modify existing feedback entries
- **Delete Feedback**: Remove feedback entries
- **Multi-language Support**: Support for feedback in different languages

## Technology Stack

- **Language**: Go 1.23.0
- **Framework**: gRPC
- **Database**: PostgreSQL
- **Protocol Buffers**: For service definition and data serialization
- **Dependencies**:
  - `google.golang.org/grpc` - gRPC framework
  - `github.com/lib/pq` - PostgreSQL driver
  - `github.com/google/uuid` - UUID generation

## Project Structure