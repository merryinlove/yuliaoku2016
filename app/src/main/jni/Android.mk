LOCAL_PATH:= $(call my-dir)

##################################################

include $(CLEAR_VARS)

LOCAL_SRC_FILES := prebuilt/libglib-2.0.so
LOCAL_MODULE:= glib
include $(PREBUILT_SHARED_LIBRARY)

##################################################

include $(CLEAR_VARS)

LOCAL_SRC_FILES := prebuilt/libgnuintl.so
LOCAL_MODULE:= intl
include $(PREBUILT_SHARED_LIBRARY)


##################################################

include $(CLEAR_VARS)

LOCAL_SRC_FILES:= \
	sdcv/lib/dictziplib.cpp \
	sdcv/lib/distance.cpp \
	sdcv/lib/file.cpp \
	sdcv/lib/lib.cpp \
	sdcv/utils.cpp \
	sdcv/libwrapper.cpp \
	sdcv/readline.cpp \
	sdcv/sdcv.cpp 

LOCAL_C_INCLUDES += \
	$(LOCAL_PATH)/sdcv \
	$(LOCAL_PATH)/include/glib \
	$(LOCAL_PATH)/sdcv/lib \
	$(LOCAL_PATH)/include		

LOCAL_CPPFLAGS += -Wno-error=format-security

LOCAL_LDLIBS := -lz

LOCAL_STATIC_LIBRARIES +=  intl  glib 

LOCAL_MODULE:= libreader

include $(BUILD_EXECUTABLE)
