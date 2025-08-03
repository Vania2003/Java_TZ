#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include "pl_e_science_git_ivahan0788_NativeSorter.h"

// --- Pomocnicze funkcje ---
jdouble getDoubleValue(JNIEnv* env, jobject doubleObj) {
    if (doubleObj == NULL) return 0;
    jclass doubleClass = (*env)->FindClass(env, "java/lang/Double");
    if (doubleClass == NULL) return 0;
    jmethodID method = (*env)->GetMethodID(env, doubleClass, "doubleValue", "()D");
    if (method == NULL) return 0;
    return (*env)->CallDoubleMethod(env, doubleObj, method);
}

jobject createDoubleObject(JNIEnv* env, jdouble value) {
    jclass doubleClass = (*env)->FindClass(env, "java/lang/Double");
    jmethodID ctor = (*env)->GetMethodID(env, doubleClass, "<init>", "(D)V");
    return (*env)->NewObject(env, doubleClass, ctor, value);
}

int compareAsc(const void* a, const void* b) {
    double diff = (*(double*)a) - (*(double*)b);
    return (diff > 0) - (diff < 0);
}

int compareDesc(const void* a, const void* b) {
    double diff = (*(double*)b) - (*(double*)a);
    return (diff > 0) - (diff < 0);
}

// --- sort01 ---
JNIEXPORT jobjectArray JNICALL Java_pl_e_1science_git_ivahan0788_NativeSorter_sort01
  (JNIEnv *env, jobject obj, jobjectArray array, jobject order)
{
    if (array == NULL || order == NULL) {
        printf("Błąd: null argument JNI\n");
        return NULL;
    }

    jsize len = (*env)->GetArrayLength(env, array);
    if (len <= 0) {
        printf("Błąd: tablica ma niepoprawną długość\n");
        return NULL;
    }

    jclass doubleClass = (*env)->FindClass(env, "java/lang/Double");
    jclass boolClass = (*env)->FindClass(env, "java/lang/Boolean");
    if (doubleClass == NULL || boolClass == NULL) {
        printf("Błąd: brak klas Double/Boolean\n");
        return NULL;
    }

    jmethodID valMethod = (*env)->GetMethodID(env, boolClass, "booleanValue", "()Z");
    jboolean orderValue = (*env)->CallBooleanMethod(env, order, valMethod);

    double* nativeArr = malloc(len * sizeof(double));
    if (nativeArr == NULL) {
        printf("malloc failed!\n");
        return NULL;
    }

    for (jsize i = 0; i < len; i++) {
        jobject element = (*env)->GetObjectArrayElement(env, array, i);
        nativeArr[i] = getDoubleValue(env, element);
    }

    qsort(nativeArr, len, sizeof(double), orderValue ? compareAsc : compareDesc);

    jobjectArray result = (*env)->NewObjectArray(env, len, doubleClass, NULL);
    for (jsize i = 0; i < len; i++) {
        jobject doubleObj = createDoubleObject(env, nativeArr[i]);
        (*env)->SetObjectArrayElement(env, result, i, doubleObj);
    }

    free(nativeArr);
    return result;
}

// --- sort02 ---
JNIEXPORT jobjectArray JNICALL Java_pl_e_1science_git_ivahan0788_NativeSorter_sort02
  (JNIEnv *env, jobject obj, jobjectArray array)
{
    jclass cls = (*env)->GetObjectClass(env, obj);
    if (cls == NULL) return NULL;

    jfieldID orderField = (*env)->GetFieldID(env, cls, "order", "Ljava/lang/Boolean;");
    if (orderField == NULL) return NULL;

    jobject order = (*env)->GetObjectField(env, obj, orderField);
    if (order == NULL) return NULL;

    return Java_pl_e_1science_git_ivahan0788_NativeSorter_sort01(env, obj, array, order);
}

// --- sort03 ---
JNIEXPORT void JNICALL Java_pl_e_1science_git_ivahan0788_NativeSorter_sort03
  (JNIEnv *env, jobject obj)
{
    int len;
    printf("Podaj liczbe elementow do sortowania: ");
    scanf("%d", &len);
    if (len <= 0 || len > 1000000) {
        printf("Nieprawidłowy rozmiar tablicy.\n");
        return;
    }

    double* data = malloc(len * sizeof(double));
    if (data == NULL) {
        printf("malloc failed!\n");
        return;
    }

    printf("Podaj %d liczb:\n", len);
    for (int i = 0; i < len; i++) {
        scanf("%lf", &data[i]);
    }

    printf("Sortować rosnąco? (1 = tak, 0 = nie): ");
    int rawOrder;
    scanf("%d", &rawOrder);

    jclass doubleClass = (*env)->FindClass(env, "java/lang/Double");
    jobjectArray jarray = (*env)->NewObjectArray(env, len, doubleClass, NULL);
    jmethodID doubleCtor = (*env)->GetMethodID(env, doubleClass, "<init>", "(D)V");

    for (int i = 0; i < len; i++) {
        jobject dObj = (*env)->NewObject(env, doubleClass, doubleCtor, data[i]);
        (*env)->SetObjectArrayElement(env, jarray, i, dObj);
    }

    jclass boolClass = (*env)->FindClass(env, "java/lang/Boolean");
    jmethodID boolCtor = (*env)->GetMethodID(env, boolClass, "<init>", "(Z)V");
    jobject jorder = (*env)->NewObject(env, boolClass, boolCtor, rawOrder != 0);

    jclass cls = (*env)->GetObjectClass(env, obj);
    jfieldID aField = (*env)->GetFieldID(env, cls, "a", "[Ljava/lang/Double;");
    jfieldID orderField = (*env)->GetFieldID(env, cls, "order", "Ljava/lang/Boolean;");

    (*env)->SetObjectField(env, obj, aField, jarray);
    (*env)->SetObjectField(env, obj, orderField, jorder);

    free(data);
}
