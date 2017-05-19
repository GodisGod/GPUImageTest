LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := gpuimage-library
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_LDLIBS := \
	-llog \

LOCAL_SRC_FILES := \
	F:\StudyDemo\GPUImageTest\library\jni\Android.mk \
	F:\StudyDemo\GPUImageTest\library\jni\Application.mk \
	F:\StudyDemo\GPUImageTest\library\jni\yuv-decoder.c \

LOCAL_C_INCLUDES += F:\StudyDemo\GPUImageTest\library\jni
LOCAL_C_INCLUDES += F:\StudyDemo\GPUImageTest\library\src\release\jni

include $(BUILD_SHARED_LIBRARY)
