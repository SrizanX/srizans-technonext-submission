# TechnoNext Android Assessment

A modern Android app showcasing posts from JSONPlaceholder API with user authentication, favorites, and search functionality.

## 🚀 Setup & Build Instructions

### Prerequisites
- Android Studio Ladybug or later
- Java 21+
- Android SDK API 34+

### Build Steps
1. Clone the repository
2. Open project in Android Studio
3. Sync project with Gradle files
4. Run the app on device/emulator

### Build Commands
```bash
# Debug build
./gradlew assembleDebug

# Run tests
./gradlew test
./gradlew connectedAndroidTest
```

## 🏗️ Architecture & Libraries

### Architecture Pattern
- **Clean Architecture** with MVVM
- **Multi-module structure** for scalability
- **Repository pattern** for data management
- **Use cases** for business logic encapsulation

### Module Structure
```
app/                    # Main application module
feature/               # Feature modules (posts, signin, signup, etc.)
core/
  ├── data/           # Repository implementations
  ├── data-cache/     # Room database & DAOs
  ├── data-network/   # Retrofit & API services
  ├── domain/         # Business logic & models
  ├── ui/             # Shared UI components
  └── design-system/  # Theme & design tokens
```

### Key Technologies
- **UI**: Jetpack Compose + Material 3
- **Networking**: Retrofit 3.0.0 + OkHttp 4.12.0
- **Database**: Room 2.7.2 + Paging 3
- **DI**: Hilt 2.57
- **Async**: Kotlin Coroutines 1.10.2 + Flow
- **Testing**: MockWebServer 5.1.0, JUnit, Mockito
- **Build**: Android Gradle Plugin 8.12.1, Kotlin 2.2.10

### Data Flow
```
UI Layer (Compose) → ViewModel → Repository → DataSource → API/Database
```

## 📱 Features

### Authentication
- **Sign Up**: Email validation, strong password requirements with real-time criteria
- **Sign In**: Email validation only (no client-side password validation for existing users)
- **Session Management**: Persistent login state with DataStore

### Posts Management
- **Feed**: Paginated posts from JSONPlaceholder API
- **Search**: Real-time search functionality
- **Favorites**: Mark/unmark posts as favorites
- **Offline**: Cached posts with Room database
- **Pull-to-refresh**: Manual refresh capability

### Data Persistence
- **Room Database**: Local caching for offline access
- **Favorites**: Persistent favorite posts storage
- **User Preferences**: Login state and user data

## ⚠️ Assumptions & Limitations

### Authentication
- **Mock Authentication**: No real backend - simulates sign-up/sign-in locally
- **Password Storage**: Uses basic preferences (not secure for production)
- **Session Timeout**: No automatic logout implementation

### API Integration
- **JSONPlaceholder**: Read-only API for posts data
- **Network Only**: User data not synced with external service
- **Rate Limiting**: No API rate limiting handling

### Data Management
- **Local Storage**: All user data stored locally
- **Cache Strategy**: Simple cache-first approach
- **Data Sync**: No conflict resolution for offline changes

### UI/UX
- **Material 3**: Follows latest design guidelines
- **Accessibility**: Basic support implemented
- **Responsive**: Optimized for phones only

### Testing
- **Unit Tests**: Core business logic covered
- **Integration Tests**: MockWebServer for network layer
- **UI Tests**: Limited Compose UI testing

## 🔒 Security Considerations

- Passwords stored in plain text (development only)
- No encryption for sensitive data
- Basic input validation implemented
- No authentication token management

## 🚧 Production Readiness

**Not production-ready** - This is a technical assessment demonstrating:
- Clean Architecture principles
- Modern Android development practices
- Testing strategies
- UI/UX design patterns

For production deployment, implement:
- Real authentication service
- Secure credential storage
- Error monitoring & analytics
- Performance optimization
- Comprehensive testing coverage