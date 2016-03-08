APP_ABI := armeabi
APP_PLATFORM := android-17
APP_CFLAGS += -Wno-error=format-security

APP_CPPFLAGS := -frtti -std=c++11  

APP_STL := gnustl_static
APP_CPPFLAGS += -fexceptions