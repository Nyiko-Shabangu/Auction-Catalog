# Rihlazana Auction Catalog Application

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=flat&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Firebase](https://img.shields.io/badge/firebase-%23039BE5.svg?style=flat&logo=firebase)](https://firebase.google.com)

## Overview

Rihlazana Auction Catalog is a modern Android application built with Kotlin that streamlines the management and presentation of auction items. This application provides a comprehensive solution for auction houses and collectors to catalog, track, and manage their items with features like real-time updates, image management, and PDF catalog generation.

## 🚀 Key Features

### Core Functionality
- **Auction Item Management**
  - Create, update, and delete auction items
  - Rich item descriptions with multiple images
  - Custom categorization and tagging
  - Real-time status updates

### Data Management
- **Firebase Integration**
  - Real-time data synchronization
  - Secure authentication system
  - Cloud storage for images and documents
  - Automated backups

### Media Handling
- **Image Management**
  - Multi-image upload support
  - Automatic image optimization
  - Thumbnail generation
  - Gallery view with zoom capabilities

### Documentation
- **PDF Generation**
  - Professional catalog creation


### User Experience
- **Modern UI/UX**
  - Material Design 3 components
  - Responsive layouts
  - Dark/Light theme support
  - Intuitive navigation

## 🛠️ Technology Stack

### Core
- **Language**: Kotlin 1.8+
- **Minimum SDK**: Android 24 (Android 7.0)
- **Target SDK**: Android 34 (Android 14)

### Libraries & Dependencies
```gradle
dependencies {
    // Firebase
    implementation platform('com.google.firebase:firebase-bom:33.6.0')
    implementation 'com.google.firebase:firebase-firestore-ktx'
    implementation 'com.google.firebase:firebase-analytics'
    implementation 'com.google.firebase:firebase-database-ktx:21.0.0'
    implementation 'com.google.firebase:firebase-storage-ktx'
    implementation 'com.google.firebase:firebase-auth:23.0.0'

    // UI Components
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
    implementation 'com.github.bumptech.glide:glide:4.15.1'

    // PDF Generation
    implementation 'com.itextpdf:itext7-core:7.2.5'

    // Android Jetpack
    implementation 'androidx.navigation:navigation-fragment-ktx:2.7.0'
    implementation 'androidx.navigation:navigation-ui-ktx:2.7.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0'
}
```

## 📥 Installation

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 11 or later
- Firebase account
- Git

### Setup Steps
1. **Clone the Repository**
   ```bash
   git clone https://github.com/IIEWFL/xbcad7319-project-submission-domain_expansion.git
   cd rihlazana-auction-catalog
   ```

2. **Firebase Configuration**
   - Create a new Firebase project
   - Add an Android app in Firebase console
   - Download `google-services.json`
   - Place it in the `app/` directory

3. **Build Configuration**
   ```bash
   # Sync project with Gradle files
   ./gradlew build
   ```

4. **Run the Application**
   - Open in Android Studio
   - Select device/emulator
   - Click "Run" (▶️)

## 🏗️ Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/rihlazana/auction/
│   │   │   ├── ui/
│   │   │   │   ├── activities/
│   │   │   │   ├── fragments/
│   │   │   │   └── adapters/
│   │   │   ├── data/
│   │   │   │   ├── models/
│   │   │   │   ├── repository/
│   │   │   │   └── datasource/
│   │   │   └── utils/
│   │   └── res/
│   └── test/
└── build.gradle
```

## 🔧 Configuration

### Firebase Rules
```json
{
  "rules": {
    "auctions": {
      ".read": "auth != null",
      ".write": "auth != null"
    }
  }
}
```

### Required Permissions
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

## 📱 Usage Guidelines

### Adding New Items
1. Navigate to "Add Item" section
2. Fill in item details
3. Upload images (up to 5 per item)
4. Set auction parameters
5. Save item

### Generating Catalogs
1. Select items for inclusion
2. Choose template style
3. Configure output options
4. Generate PDF
5. Share or save locally

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

- Documentation: [docs.rihlazana.com](https://docs.rihlazana.com)
- Issues: [GitHub Issues](https://github.com/your-organization/rihlazana-auction-catalog/issues)
- Email: support@rihlazana.com



[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/xq-9uqGN)
