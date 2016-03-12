//
// Created by 姜雷 on 16/3/10.
//

#include "config.h"
#include "reader.h"

#include <cerrno>
#include <cstring>
#include <cstdlib>
#include <cstdio>
#include <clocale>
#include <string>
#include <vector>
#include <memory>

#include <android/log.h>
#include <unistd.h>

#include <glib.h>
#include <glib/gi18n.h>
#include <glib/gstdio.h>
#include <jni.h>
#include <glib/glibintl.h>

#include "libwrapper.hpp"
#include "readline.hpp"
#include "utils.hpp"

static const char gVersion[] = VERSION;

static const gchar *stardict_data_dir = "/sdcard/.yykdict";
static const char *homedir = "/data/data/com.xya.csu/files";
static strlist_t disable_list;
std::string data_dir = stardict_data_dir;

const strlist_t dicts_dir_list = {std::string(homedir) + G_DIR_SEPARATOR
                                  + "dic", data_dir};

namespace {
    static void free_str_array(gchar **arr) {
        gchar **p;

        for (p = arr; *p; ++p)
            g_free(*p);
        g_free(arr);
    }
}
namespace glib {
    typedef ResourceWrapper<gchar *, gchar *, free_str_array> StrArr;
}

char *jstringTostring(JNIEnv *env, jstring jstr) {
    char *rtn = NULL;
    jclass clsstring = env->FindClass("java/lang/String");
    jstring strencode = env->NewStringUTF("utf-8");
    jmethodID mid = env->GetMethodID(clsstring, "getBytes",
                                     "(Ljava/lang/String;)[B");
    jbyteArray barr = (jbyteArray) env->CallObjectMethod(jstr, mid, strencode);
    jsize alen = env->GetArrayLength(barr);
    jbyte *ba = env->GetByteArrayElements(barr, JNI_FALSE);
    if (alen > 0) {
        rtn = (char *) malloc(alen + 1);
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;
    }
    env->ReleaseByteArrayElements(barr, ba, 0);
    return rtn;
}

jstring strTojstring(JNIEnv *env, const char *pat) {
    //定义java String类 strClass
    jclass strClass = (env)->FindClass("java/lang/String");
    //获取String(byte[],String)的构造器,用于将本地byte[]数组转换为一个新String
    jmethodID ctorID = (env)->GetMethodID(strClass, "<init>",
                                          "([BLjava/lang/String;)V");
    //建立byte数组
    jbyteArray bytes = (env)->NewByteArray(strlen(pat));
    //将char* 转换为byte数组
    (env)->SetByteArrayRegion(bytes, 0, strlen(pat), (jbyte *) pat);
    // 设置String, 保存语言类型,用于byte数组转换至String时的参数
    jstring encoding = (env)->NewStringUTF("utf-8");
    //将byte数组转换为java String,并输出
    return (jstring) (env)->NewObject(strClass, ctorID, bytes, encoding);
}

/*
 * Class:     com_xya_csu_utility_yykReader
 * Method:    setDataDir
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_xya_csu_utility_YykReader_setDataDir(
        JNIEnv *env, jobject obj, jstring string) {
    stardict_data_dir = jstringTostring(env, string);
}

/*
 * Class:     com_xya_csu_utility_yykReader
 * Method:    listDict
 * Signature: ()[Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_xya_csu_utility_YykReader_listDict(
        JNIEnv *env, jobject obj) {

    string result = "";

    strlist_t order_list, disable_list;

    for_each_file(dicts_dir_list, ".ifo", order_list, disable_list,
                  [&](const std::string &filename, bool) -> void {
                      DictInfo dict_info;
                      if (dict_info.load_from_ifo_file(filename, false)) {
                          const std::string bookname = utf8_to_locale_ign_err(dict_info.bookname);
                          result = result + "." + bookname.c_str() + ",";//dict_info.wordcount;
                          char buf[256];
                          sprintf(buf, "%d", dict_info.wordcount);
                          string b = buf;
                          result = result + buf + "\n";
                          //result.append((const char *) dict_info.wordcount);
                      }
                  });
    return env->NewStringUTF(result.c_str());
}

/*
 * Class:     com_xya_csu_utility_yykReader
 * Method:    useDict
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_xya_csu_utility_YykReader_useDict(JNIEnv *env,
                                                                  jobject obj, jstring string) {
    glib::StrArr use_dict_list;
    get_addr(use_dict_list);
    strlist_t empty_list;
    for_each_file(dicts_dir_list, ".ifo", empty_list, empty_list,
                  [&](const std::string &filename, bool) -> void {
                      DictInfo dict_info;
                      const bool load_ok = dict_info.load_from_ifo_file(filename, false);
                      if (!load_ok)
                          return;

                      for (gchar **p = get_impl(use_dict_list); *p != nullptr; ++p)
                          if (strcmp(*p, dict_info.bookname.c_str()) == 0)
                              return;
                      disable_list.push_back(dict_info.ifo_file_name);
                  });
}

/*
 * Class:     com_xya_csu_utility_yykReader
 * Method:    searchKey
 * Signature: (Ljava/lang/String;)[Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_xya_csu_utility_YykReader_searchKey(
        JNIEnv *env, jobject obj, jstring string) {
    const std::string conf_dir = std::string(homedir) + G_DIR_SEPARATOR
                                 + ".stardict";
    if (g_mkdir(conf_dir.c_str(), S_IRWXU) == -1 && errno != EEXIST)
        __android_log_print(ANDROID_LOG_ERROR, "error-list", _("g_mkdir failed: %s\n"),
                            strerror(errno));
    Library lib(TRUE, FALSE, FALSE);
    strlist_t empty_list;
    lib.load(dicts_dir_list, empty_list, disable_list);
    std::unique_ptr<IReadLine> io(create_readline_object());
    char *result = jstringTostring(env, string);
    const char *success = lib.process_phrase(result, *io, FALSE);
    __android_log_print(ANDROID_LOG_ERROR, "demo", success);
    return env->NewStringUTF(success);
}
