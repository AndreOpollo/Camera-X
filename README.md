# 📷 Camera-X App
A modern Android camera application built using CameraX and Jetpack Compose, packed with essential and creative photography features. Designed for smooth performance and high-quality captures on physical Android devices.

## ✨ Features
### 🔁 Camera Switching

- Toggle between front and back cameras with a single tap.

### 🔍 Pinch to Zoom

- Use two fingers to zoom in and out smoothly. You can also manually adjust the zoom index via the slider in the advanced controls.

### 🎯 Tap to Focus

- Tap on any area of the preview to focus the camera.

### ⚡ Flash Modes

- Easily switch between Auto, On, and Off flash modes.

### 🎨 Photo Filters

- Apply real-time editing filters like:

  - Sepia

  - Grayscale

  - Polaroid

  - Contrast

- and more...

### 🌗 Exposure Control

- Maximize or minimize exposure levels manually.

### ⏱ Timer Capture

- Take photos after a 3s or 10s delay using the built-in timer.

### 🧮 Grid Overlay

- Enable/disable a composition grid to aid better framing.

### 👆 Double-Tap to Hide Controls

- Double-tap the screen to toggle UI controls for an immersive experience.

### ⚡ Speed vs Latency

- Switch between fast capture or high-quality low-latency modes.

### 💾 Photo Management

- Save photos to the gallery

- Share via other apps

- Edit with filters

- Delete unwanted shots

# 🛠 Tech Stack

| Tech            | Purpose                                                          |
| -----------     | ---------------------------------------------------------------- |
| Kotlin          | The modern Android development language                          |
| CameraX         | For modern camera functionality                                  |
| Jetpack Compose | Declarative UI toolkit                                           |
| MediaStore API  | Save media in scoped storage                                     |
| Dagger Hilt     | Dependency injection                                             |
| Material 3      | Modern UI Styling                                                |
| Coil            | Effecient Image Loading                                          |



## 🚀 Setup & Installation
⚠️ This app is intended to run on a physical Android device due to camera and MediaStore dependencies. Emulator support is not guaranteed.

## ✅ Prerequisites
- Android Studio Hedgehog or newer

- Android device running API 26+

- Kotlin 1.9+

- Enable developer options and USB debugging on your device

## 🔧 Steps
- Clone the repository
```
git clone https://github.com/AndreOpollo/Camera-X.git
```
- Open the project in Android Studio

- Connect your Android device via USB

- Run the app


## 💡 Notes
- Make sure to grant Camera and Storage permissions on first launch.

- Filters are implemented via ColorMatrix for real-time preview and editing.

- Use MediaStore to ensure scoped storage compatibility on Android 10+.

## 📸 Screenshots

<p>
  <img src="https://github.com/user-attachments/assets/8b0a5f30-bbcd-4a24-87ae-8165de57a50a" width="300" style="margin-right:16px;"/>
  <img src="https://github.com/user-attachments/assets/1875dc2a-bf69-4007-9587-971f26e8ee74" width="300"/>
</p>
<p>
  <img src="https://github.com/user-attachments/assets/fc94e213-9f30-4a01-a413-f892299be5de" width="300" style="margin-right:16px;"/>
  <img src="https://github.com/user-attachments/assets/104eceb6-2d7b-4ae6-b11f-30cfa8e40805" width="300"/>
</p>
<p>
  <img src="https://github.com/user-attachments/assets/bec75a17-981f-40ec-b274-941ef9976b61" width="300" style="margin-right:16px;"/>
  <img src="https://github.com/user-attachments/assets/1d84bc2e-16f6-4cfb-9b0b-a6ddf735e45d" width="300"/>
</p>









