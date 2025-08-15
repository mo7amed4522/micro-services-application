# Chat Services

A comprehensive gRPC-based microservice for real-time chat functionality within the Luxor application ecosystem. This service provides advanced messaging capabilities including real-time streaming, video/audio calling, notifications, and group management.

## Overview

The Chat Service is a feature-rich communication platform that supports both traditional request-response messaging and real-time streaming communication. It integrates with WebRTC for peer-to-peer video and audio calling, includes push notifications via OneSignal, and provides comprehensive chat management features.

## Key Features

### Core Messaging
- **Real-time Chat Streaming**: Bidirectional streaming for instant message delivery
- **Message Management**: Send, edit, delete, forward, pin/unpin messages
- **Chat History**: Retrieve paginated chat history with search functionality
- **Read Receipts**: Mark messages as read with delivery confirmation
- **Message Reactions**: Like and manage message reactions
- **Scheduled Messages**: Schedule messages for future delivery

### Advanced Communication
- **Video Calling**: WebRTC-based video calls with streaming support
- **Audio Calling**: High-quality audio communication
- **Call Management**: Initiate, accept, reject, and end calls
- **Call History**: Track and retrieve call records
- **Call Signaling**: WebRTC signaling for peer connection establishment

### Group Management
- **Group Creation**: Create and configure chat groups
- **Group Administration**: Join, leave, and update group settings
- **Member Management**: Add/remove group members
- **Group Discovery**: Find groups by user email

### Real-time Features
- **Live User Status**: Track online/offline status and last seen
- **Typing Indicators**: Real-time typing status updates
- **Push Notifications**: OneSignal integration for mobile notifications
- **Last Message Subscriptions**: Real-time updates for conversation previews

### Additional Features
- **Message Search**: Full-text search across chat history
- **Pinned Messages**: Pin important messages in conversations
- **User Discovery**: Find users by email address
- **Notification Management**: Comprehensive notification system

## Technology Stack

- **Language**: Go 1.23.0
- **Framework**: gRPC with streaming support
- **Database**: PostgreSQL
- **Real-time Communication**: WebRTC
- **Push Notifications**: OneSignal
- **Authentication**: JWT token validation via User Service
- **Protocol Buffers**: For service definition and data serialization

### Dependencies

```go
require (
    github.com/google/uuid v1.6.0
    github.com/lib/pq v1.10.9
    google.golang.org/grpc v1.74.2
    google.golang.org/protobuf v1.36.6
    github.com/OneSignal/onesignal-go-api v1.0.4
    user-services v0.0.0 // Local dependency
)
```

## Project Structure