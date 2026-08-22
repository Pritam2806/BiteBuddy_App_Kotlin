# 🍕 BiteBuddy — Food Ordering & Restaurant Management System

<p align="center">
  <img src="app/src/main/res/mipmap-xxhdpi/ic_launcher.webp" alt="BiteBuddy Logo" width="100"/>
</p>

<p align="center">
  <strong>A modern, full-stack food ordering platform connecting hungry customers with local restaurants in real time.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white" alt="Platform"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white" alt="Jetpack Compose"/>
  <img src="https://img.shields.io/badge/Design-Material%203%20Dark%20Mode-FFBA00" alt="Material 3"/>
  <img src="https://img.shields.io/badge/Backend-Firebase-FFCA28?logo=firebase&logoColor=black" alt="Firebase"/>
  <img src="https://img.shields.io/badge/Min%20SDK-24%20(Android%207.0)-informational" alt="Min SDK"/>
</p>

<p align="center">
  <a href="#-restaurant-web-application--github-repo">🌐 Web App & Repo</a> •
  <a href="#-app-screenshots">📱 Screenshots</a> •
  <a href="#-video-demo">🎥 Video Demo</a> •
  <a href="#-apk-download">📦 Download APK</a> •
  <a href="#-getting-started">🚀 Getting Started</a>
</p>

---

## 📌 Project Overview

**BiteBuddy** is a multi-platform food ordering ecosystem consisting of:
1. **Android Customer Mobile Application** (Built with **Kotlin + Jetpack Compose**): Enables customers to discover active restaurants, browse delicious menus, customize food items, manage single-restaurant carts, place orders via Cash on Delivery, and track live status updates in real time.
2. **Restaurant Web Dashboard** (Built with **React + TypeScript**): Enables restaurant owners to register, manage menu items, toggle live item availability, update prices, and process incoming customer orders.
3. **Shared Firebase Backend**: Cloud Firestore (real-time listeners), Firebase Authentication, Firebase Cloud Functions (trusted server-side order calculation), Firebase Cloud Messaging (FCM push notifications), and Firebase Storage.

---

## 🌐 Restaurant Web Application & GitHub Repo

The restaurant management web dashboard allows restaurant owners to register restaurants, manage menus, toggle food availability, and accept/dispatch orders in real time.

| Resource | Link |
| :--- | :--- |
| **🌐 Live Web App** | [![Live Website](https://img.shields.io/badge/Website-Live%20Demo-00C7B7?style=for-the-badge&logo=vercel&logoColor=white)](https://your-restaurant-web-app.vercel.app) |
| **💻 Web App GitHub Repo** | [![Web GitHub](https://img.shields.io/badge/GitHub-Restaurant%20Web%20Dashboard-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/your-friend-username/restaurant-web-app) |
| **📱 Android App GitHub Repo** | [![Android GitHub](https://img.shields.io/badge/GitHub-Android%20Customer%20App-7F52FF?style=for-the-badge&logo=github&logoColor=white)](https://github.com/your-username/BiteBuddy) |

> *(Replace the links above with your friend's live website URL and GitHub repository link).*

---

## 📱 App Screenshots

<table align="center">
  <tr>
    <td align="center" width="25%">
      <strong>1. Welcome / Sign In</strong><br/><br/>
      <img src="screenshots/01_signin.jpg" alt="Sign In" width="220"/>
    </td>
    <td align="center" width="25%">
      <strong>2. Sign Up</strong><br/><br/>
      <img src="screenshots/02_signup.jpg" alt="Sign Up" width="220"/>
    </td>
    <td align="center" width="25%">
      <strong>3. Home / Discover</strong><br/><br/>
      <img src="screenshots/03_home.jpg" alt="Home Screen" width="220"/>
    </td>
    <td align="center" width="25%">
      <strong>4. Restaurant Menu</strong><br/><br/>
      <img src="screenshots/04_menu.jpg" alt="Restaurant Menu" width="220"/>
    </td>
  </tr>
  <tr>
    <td align="center" width="25%">
      <strong>5. Food Item Detail</strong><br/><br/>
      <img src="screenshots/05_food_detail.jpg" alt="Food Detail Modal" width="220"/>
    </td>
    <td align="center" width="25%">
      <strong>6. Order Review / Cart</strong><br/><br/>
      <img src="screenshots/06_cart.jpg" alt="Cart Screen" width="220"/>
    </td>
    <td align="center" width="25%">
      <strong>7. Checkout (COD)</strong><br/><br/>
      <img src="screenshots/07_checkout.jpg" alt="Checkout Screen" width="220"/>
    </td>
    <td align="center" width="25%">
      <strong>8. Live Order Tracking</strong><br/><br/>
      <img src="screenshots/08_tracking.jpg" alt="Active Order Live Tracking" width="220"/>
    </td>
  </tr>
  <tr>
    <td align="center" width="25%">
      <strong>9. Order History</strong><br/><br/>
      <img src="screenshots/09_history.jpg" alt="Order History" width="220"/>
    </td>
    <td align="center" width="25%">
      <strong>10. User Profile</strong><br/><br/>
      <img src="screenshots/10_profile.jpg" alt="Profile Screen" width="220"/>
    </td>
    <td align="center" width="25%">
      <strong>11. Delivery Address</strong><br/><br/>
      <img src="screenshots/11_address.jpg" alt="Address Management" width="220"/>
    </td>
    <td align="center" width="25%">
      <!-- Placeholder for future 12th screen -->
      <strong>12. Food Options & Stepper</strong><br/><br/>
      <img src="screenshots/05_food_detail.jpg" alt="Customizer" width="220"/>
    </td>
  </tr>
</table>

---

## 🎥 Video Demo

<!-- Replace the link below with your YouTube / Loom video link or embed an MP4 / GIF -->
[![Watch Demo Video](https://img.shields.io/badge/▶%20Watch%20Demo-App%20Walkthrough-FF0000?style=for-the-badge&logo=youtube&logoColor=white)](https://your-video-link-here.com)

> 📹 **Demo Video Preview:** [Click here to watch the full video walkthrough](https://your-video-link-here.com) *(or place a demo `.mp4` / `.gif` in your repo)*.

---

## 📦 APK Download

Download the latest pre-compiled debug APK directly:

[![Download APK](https://img.shields.io/badge/Download-BiteBuddy%20APK-FFBA00?style=for-the-badge&logo=android&logoColor=black)](https://github.com/your-username/BiteBuddy/releases/latest)

- **Direct Build Location:** `app/build/outputs/apk/debug/app-debug.apk`
- **Minimum Android Version:** Android 7.0 (API Level 24)
- **Target Android Version:** Android 14 (API Level 34)

---

## ✨ Key Features (Android Customer App)

- 🌙 **Modern Dark Mode UI:** High-contrast Midnight Charcoal (`#12161A`) background with sleek cards and vibrant Golden Yellow (`#FFBA00`) accents inspired by modern delivery apps.
- 🔐 **Firebase Authentication:** Secure customer registration and login with session persistence, automatic profile sync, and delivery address capture.
- 🏪 **Live Restaurant Discovery:** Real-time query of active restaurants (`whereEqualTo("status", "active")`) with ratings, distance, delivery estimates, and category filters.
- 📋 **Interactive Menu & Dish Customization:**
  - Real-time stock status (`isAvailable` badges).
  - Food detail sheet with nutritional tags (Prep time, Calories, Weight), portion size selector (Small, Medium, Large), and circular quantity steppers `[- 2 +]`.
- 🛒 **Single-Restaurant Cart Enforcement:** Prevents mixed-restaurant carts by prompting a clean confirmation dialog before replacing cart items.
- 💳 **Cash on Delivery (COD) Checkout:** Clear bill breakdown with transparent itemized costs, free delivery badge, and in-place delivery address editing.
- 🔄 **Real-Time 3-Step Order Tracking:**
  - Emits real-time status updates via Kotlin `Flow` and Firestore snapshot listeners.
  - **3 Customer-Facing States:**
    $$\text{Order Placed} \longrightarrow \text{Order Accepted} \longrightarrow \text{Out for Delivery}$$
  - *(Internal kitchen preparation is managed during the Accepted state—no confusing intermediate statuses for the user!)*
- 🔔 **Firebase Cloud Messaging (FCM):** Push notifications for order acceptance and out-for-delivery events with automatic deep-linking to the active tracking screen.
- 📜 **Order History & Profile:** View past orders, track ongoing deliveries, manage default delivery locations, and update user preferences.

---

## 🔄 Order Lifecycle & State Machine

```
┌─────────────────────────┐
│     Customer Places     │
│          Order          │
└────────────┬────────────┘
             │
             ▼
      ┌─────────────┐
      │   PLACED    │  • Order document created in Firestore
      └──────┬──────┘  • App shows "Waiting for restaurant to accept..."
             │
             │ Restaurant clicks "Accept Order" on Web Dashboard
             ▼
      ┌─────────────┐
      │  ACCEPTED   │  • Food prepared internally by kitchen
      └──────┬──────┘  • Customer sees "Order Accepted" + FCM Push received
             │
             │ Restaurant clicks "Out for Delivery" on Web Dashboard
             ▼
   ┌──────────────────┐
   │ OUT_FOR_DELIVERY │  • Food handed over to delivery
   └──────────────────┘  • Customer sees "Out for Delivery" + FCM Push received
```

---

## 🏗️ Architecture & Project Structure

The project follows modern **Android Clean Architecture** with **MVVM (Model-View-ViewModel)** and unidirectional data flow:

```text
com.example.bitebuddy/
├── data/
│   ├── firebase/       # Firebase singletons & optional emulator config
│   │   └── FirebaseModule.kt
│   ├── model/          # Kotlin data models (UserProfile, Restaurant, MenuItem, Order, etc.)
│   │   ├── Address.kt
│   │   ├── AppNotification.kt
│   │   ├── MenuItem.kt
│   │   ├── Order.kt
│   │   ├── Resource.kt
│   │   ├── Restaurant.kt
│   │   └── UserProfile.kt
│   └── repository/     # Data sources & business rules
│       ├── AuthRepository.kt
│       ├── CartRepository.kt
│       ├── MenuRepository.kt
│       ├── OrderRepository.kt
│       ├── RestaurantRepository.kt
│       └── UserRepository.kt
├── navigation/         # Compose Navigation host & route definitions
│   └── AppNavigation.kt
├── service/            # Firebase Cloud Messaging background service
│   └── BiteBuddyMessagingService.kt
├── ui/
│   ├── components/     # Reusable UI widgets (Buttons, TextFields, Steppers, Cards, Chips)
│   ├── screens/        # 12+ App screens (Auth, Home, Menu, Cart, Checkout, Tracking, Profile)
│   ├── theme/          # Custom Dark Mode color tokens, typography & theme
│   └── viewmodel/      # Architecture ViewModels with StateFlow
└── MainActivity.kt     # Single Activity entry point & permission handler
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17 or higher
- Android SDK 34
- A Firebase project with Firestore, Authentication, Functions, and Cloud Messaging enabled

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/BiteBuddy.git
cd BiteBuddy
```

### 2. Configure Firebase
1. Download your `google-services.json` from the [Firebase Console](https://console.firebase.google.com/).
2. Place the file in the `app/` directory:
   ```text
   BiteBuddy/app/google-services.json
   ```

### 3. Build & Run
- **Via Android Studio:** Open the project, sync Gradle, and click **Run** (Shift + F10) on an emulator or physical device.
- **Via Command Line (PowerShell / Terminal):**
  ```powershell
  # Compile debug APK
  .\gradlew.bat assembleDebug

  # Install directly to connected device
  .\gradlew.bat installDebug
  ```

---

## 🔒 Security & Data Integrity

- **Server-Side Price Validation:** Final totals and item availability are verified on the backend via the `createOrder` Cloud Function to prevent client-side price tampering.
- **Single-Restaurant Cart:** Restricts orders to a single restaurant to prevent invalid cross-vendor order states.
- **Strict Role-Based Firestore Rules:** Customers have read-only access to restaurants and menu items and write access limited to their own profile and order requests.

---

## 👥 Contributors & Collaboration

| Role | Platform | Repository & Live Links |
| :--- | :--- | :--- |
| **Android Mobile Developer** | Kotlin + Jetpack Compose | [📱 Android Customer App Repo](https://github.com/your-username/BiteBuddy) |
| **Fullstack Web Developer** | React + TypeScript + Firebase | [🌐 Live Web App](https://your-restaurant-web-app.vercel.app) • [💻 Web Dashboard Repo](https://github.com/your-friend-username/restaurant-web-app) |

---

<p align="center">Made with ❤️ for food lovers everywhere.</p>
