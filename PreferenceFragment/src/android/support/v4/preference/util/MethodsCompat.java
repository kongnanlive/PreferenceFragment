package android.support.v4.preference.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;

public class MethodsCompat {

    private static final Method getMeasuredState;
    private static final Field mMeasuredWidth, mMeasuredHeight;

    static {
        getMeasuredState = getMethod(View.class, "getMeasuredState");
        if (getMeasuredState == null) {
            mMeasuredWidth = getField(View.class, "mMeasuredWidth");
            mMeasuredHeight = getField(View.class, "mMeasuredHeight");
        } else {
            mMeasuredWidth = null;
            mMeasuredHeight = null;
        }
    }

    public static final int getMeasuredState(View view) {
        if (getMeasuredState == null) {
            final int MEASURED_STATE_MASK = 0xff000000;
            final int MEASURED_HEIGHT_STATE_SHIFT = 16;
            return (getIntFieldValue(view, mMeasuredWidth) & MEASURED_STATE_MASK)
                    | ((getIntFieldValue(view, mMeasuredHeight) >> MEASURED_HEIGHT_STATE_SHIFT)
                    & (MEASURED_STATE_MASK >> MEASURED_HEIGHT_STATE_SHIFT));
        }
        return callMethod(getMeasuredState, view);
    }

    static final Field getField(Class clazz, String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            if (field != null && !field.isAccessible()) {
                field.setAccessible(true);
            }
        } catch (NoSuchFieldException e) {
        }
        return field;
    }

    static final int getIntFieldValue(Object obj, Field field) {
        try {
            return field.getInt(obj);
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
        return 0;
    }

    /* private */
    static int callMethod(Method method, Object obj) {
        if (method != null && obj != null) {
            try {
                Object ret = method.invoke(obj, null);
                if (ret != null && ret instanceof Integer) {
                    ((Integer) ret).intValue();
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return 0;
    }

    /* private */
    static final Method getMethod(Class clazz, String methodName) {
        Method method = null;
        try {
            method = clazz.getMethod(methodName, null);
            if (method != null && !method.isAccessible()) {
                method.setAccessible(true);
            }
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
        }
        return method;
    }

    public byte[] copyOfRange(byte[] from, int start, int end) {
        int length = end - start;
        byte[] result = new byte[length];
        System.arraycopy(from, start, result, 0, length);
        return result;
    }

    public static boolean[] copyOfRange(boolean[] original, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int originalLength = original.length;
        if (start < 0 || start > originalLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int resultLength = end - start;
        int copyLength = Math.min(resultLength, originalLength - start);
        boolean[] result = new boolean[resultLength];
        System.arraycopy(original, start, result, 0, copyLength);
        return result;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Set<String> getValue(SharedPreferences sp, String key,
                                       Set<String> defValue) {
        try {
            if (Build.VERSION.SDK_INT < 11) {
                final String regularEx = "|";
                String setString = sp.getString(key, null);
                if (setString != null) {
                    String[] values = setString.split(regularEx);
                    Set<String> set = new HashSet<String>(values.length);
                    for (String value : values) {
                        if (!TextUtils.isEmpty(value))
                            set.add(value);
                    }
                    return set;
                }
            } else {
                return sp.getStringSet(key, defValue);
            }

        } catch (Throwable e) {
        }
        return defValue;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void putStringSet(SharedPreferences.Editor edit, String key,
                                    Set<String> values) {
        try {
            if (Build.VERSION.SDK_INT < 11) {
                final String regularEx = "|";
                String str = "";
                if (values != null && !values.isEmpty()) {
                    Object[] objects = values.toArray();
                    for (Object obj : objects) {
                        str += obj.toString();
                        str += regularEx;
                    }
                    edit.putString(key, str);
                }
            } else {
                edit.putStringSet(key, values).commit();
            }
        } catch (Throwable e) {
        }
    }

    public static void apply(SharedPreferences.Editor editor) {
        try {
            Method sApplyMethod = SharedPreferences.Editor.class.getMethod(
                    "apply", new Class[0]);
            sApplyMethod.invoke(editor, new Object[0]);
            return;
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        editor.commit();
    }

    @TargetApi(5)
    public static void overridePendingTransition(Activity activity,
                                                 int enter_anim, int exit_anim) {
        activity.overridePendingTransition(enter_anim, exit_anim);
    }

    @TargetApi(7)
    public static Bitmap getThumbnail(ContentResolver cr, long origId,
                                      int kind, Options options) {
        return MediaStore.Images.Thumbnails.getThumbnail(cr, origId, kind,
                options);
    }

    @TargetApi(8)
    public static File getExternalCacheDir(Context context) {

        // // return context.getExternalCacheDir(); API level 8
        //
        // // e.g. "<sdcard>/Android/data/<package_name>/cache/"
        // final File extCacheDir = new
        // File(Environment.getExternalStorageDirectory(),
        // "/Android/data/" + context.getApplicationInfo().packageName +
        // "/cache/");
        // extCacheDir.mkdirs();
        // return extCacheDir;

        return context.getExternalCacheDir();
    }

    @TargetApi(11)
    public static void recreate(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            activity.recreate();
        }
    }

    @TargetApi(11)
    public static void setLayerType(View view, int layerType, Paint paint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            view.setLayerType(layerType, paint);
        }
    }

    @TargetApi(14)
    public static void setUiOptions(Window window, int uiOptions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            window.setUiOptions(uiOptions);
        }
    }

}