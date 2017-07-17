package com.wch.libs.demo;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.wch.libs.R;
import com.wch.libs.activity.BaseActivity;
import com.wch.libs.views.imagecheck.imageloader.ImageLoaderMainActivity;
import com.wch.libs.views.zxing.BitmapLuminanceSource;
import com.wch.libs.views.zxing.CameraManager;
import com.wch.libs.views.zxing.CaptureActivityHandler;
import com.wch.libs.views.zxing.DecodeFormatManager;
import com.wch.libs.views.zxing.InactivityTimer;
import com.wch.libs.views.zxing.ViewfinderView;

public class ScanActivity extends BaseActivity implements View.OnClickListener, SurfaceHolder.Callback {

        // 是否要初始化摄像头的标记
        private boolean hasSurface;
        private InactivityTimer inactivityTimer;
        private Vector<BarcodeFormat> decodeFormats;
        private String characterSet;
        private CaptureActivityHandler handler;
        // 是否播放音乐
        private boolean playBeep;
        private MediaPlayer mediaPlayer;
        // 是否震动
        private boolean vibrate;
        // 震动的时长
        private static final long VIBRATE_DURATION = 200L;
        // 扫描视图
        private ViewfinderView viewfinderView;
        // 响铃
        private static final float BEEP_VOLUME = 0.10f;

        @Override
        protected void onCreateActivity(Bundle arg0) {
            super.onCreateActivity(arg0);
            setContentView(R.layout.demo_zxing);
        }

        /**
         * 初始化视图
         */
        @Override
        protected void initViews() {
            CameraManager.init(getApplication());
            hasSurface = false;
            inactivityTimer = new InactivityTimer(this);
            // 注册摄像头崩溃广播
            IntentFilter filter = new IntentFilter(CameraManager.INTENT_ACTION_PREVIEW);
            registerReceiver(mReceiver, filter);
            viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
            findViewById(R.id.top_bar_left_layout).setOnClickListener(this);
            findViewById(R.id.top_bar_right_layout).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.top_bar_left_layout) {
                this.finish();

            } else if (i == R.id.top_bar_right_layout) {// 相册选图
                Intent intent = new Intent(this, ImageLoaderMainActivity.class);
                startActivityForResult(intent, 0);
            }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                switch (requestCode) {
                    case 0:
                        if (data == null) {
                            mHandler.sendEmptyMessage(1);
                            return;
                        }
                        ArrayList<String> imagelist = data
                                .getStringArrayListExtra("selectedImage");
                        String mDirPath = data.getStringExtra("mDirPath");
                        if (imagelist == null || imagelist.size() < 1) {
                            mHandler.sendEmptyMessage(1);
                            return;
                        } else {
                            // imageUrl = mDirPath + "/" + imagelist.get(0);
                            // imageName = imagelist.get(0);
                            Result back = scanningImage(mDirPath + "/" + imagelist.get(0));
                            if(back==null){
                                Toast("没有扫描到");
                            }else{
                                Toast(back.getText());
                            }
                        }
                break;
                    default:
                        break;
                }
        }

        Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        // 扫码成功
                        handleDecode((String) msg.obj);
                        break;
                    case 1:
                        // 扫码失败
                        Toast("无法识别，轻触屏幕继续扫码");
                        break;
                    default:
                        break;
                }
            }
        };

        /**
         * 中文乱码
         *
         * 暂时解决大部分的中文乱码 但是还有部分的乱码无法解决 .
         *
         * @return
         */
        private String recode(String str) {
            String formart = "";
            try {
                boolean ISO = Charset.forName("ISO-8859-1").newEncoder().canEncode(str);
                if (ISO) {
                    formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
                } else {
                    formart = str;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return formart;
        }

        /**
         * 解码部分
         *
         * @param path
         * @return
         */
        private Result scanningImage(String path) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true; // 先获取原大小
            Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
            options.inJustDecodeBounds = false; // 获取新的大小
            int sampleSize = (int) (options.outHeight / (float) 200);
            if (sampleSize <= 0)
                sampleSize = 1;
            options.inSampleSize = sampleSize;
            scanBitmap = BitmapFactory.decodeFile(path, options);

            if (scanBitmap != null) {
                BitmapLuminanceSource source = new BitmapLuminanceSource(scanBitmap);
                BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));

                MultiFormatReader multiFormatReader = new MultiFormatReader();
                // 解码的参数
                Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);
                // 可以解析的编码类型
                Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
                decodeFormats = new Vector<BarcodeFormat>();
                if (decodeFormats == null || decodeFormats.isEmpty()) {
                    // 这里设置可扫描的类型，我这里选择了都支持
                    decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
                    decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
                    decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
                    hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
                }
                // 设置继续的字符编码格式为UTF8
                // hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
                // 设置解析配置参数
                multiFormatReader.setHints(hints);
                // 开始对图像资源解码
                Result rawResult = null;
                try {
                    rawResult = multiFormatReader.decode(bitmap1);
                    return rawResult;
                } catch (NotFoundException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
            return null;
        }

        /**
         * 接收摄像头崩溃广播 后finish
         */
        private BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(CameraManager.INTENT_ACTION_PREVIEW)) {
                    finish();
                }
            }
        };

        protected void onDestroy() {
            inactivityTimer.shutdown();
            if (mReceiver != null) {
                unregisterReceiver(mReceiver);
            }
            super.onDestroy();
        };

        @Override
        protected void onPause() {
            super.onPause();
            if (handler != null) {
                handler.quitSynchronously();
                handler = null;
            }
            CameraManager.get().closeDriver();
        }

        @Override
        protected void onResume() {
            super.onResume();
            initSurfaceView();
        }

        /**
         * 初始化SurfaceView
         */
        private void initSurfaceView() {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            if (hasSurface) {
                initCamera(surfaceHolder);
            } else {
                surfaceHolder.addCallback(this);
                surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            }
            decodeFormats = null;
            characterSet = null;
            playBeep = true;
            AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
            if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
                playBeep = false;
            }
            initBeepSound();
            vibrate = true;
        }

        /**
         * 初始化相机
         */
        private void initCamera(SurfaceHolder surfaceHolder) {
            try {
                CameraManager.get().openDriver(surfaceHolder);
            } catch (IOException ioe) {
                return;
            } catch (RuntimeException e) {
                return;
            }
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (!hasSurface) {
                hasSurface = true;
                initCamera(holder);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            hasSurface = false;
        }

        /**
         * 初始化音乐播放
         */
        private void initBeepSound() {
            if (playBeep && mediaPlayer == null) {
                setVolumeControlStream(AudioManager.STREAM_MUSIC);
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnCompletionListener(beepListener);

                AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
                try {
                    mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                    file.close();
                    mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    mediaPlayer = null;
                }
            }
        }

        /**
         * 播放声音并震动
         */
        private void playBeepSoundAndVibrate() {
            if (playBeep && mediaPlayer != null) {
                mediaPlayer.start();
            }
            if (vibrate) {
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(VIBRATE_DURATION);
            }
        }

        private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.seekTo(0);
            }
        };

        // 扫码结果
        private String result;

        /**
         * 扫码成功后返回的方法
         */
        public void handleDecode(Result rawResult, Bitmap barcodeBitmap) {
            inactivityTimer.onActivity();
            playBeepSoundAndVibrate();
            String result = rawResult.getText();
            handleDecode(result);
        }

        /**
         * 处理扫描结果
         *
         * @param result
         */
        private void handleDecode(String result) {
            Toast(result);
        }

        /**
         * 非网络状态的触发事件
         */
        android.content.DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.sendEmptyMessageDelayed(R.id.restart_preview, 2 * 1000);
            }
        };

        android.content.DialogInterface.OnClickListener mPosListener = new android.content.DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 调用系统默认浏览器打开网页
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(result);
                intent.setData(content_url);
                startActivity(intent);
            }
        };

        /**
         *
         * @return
         */
        public ViewfinderView getViewfinderView() {
            return viewfinderView;
        }

        /**
         * 获取handler
         */
        public Handler getHandler() {
            return handler;
        }

        /**
         *
         */
        public void drawViewfinder() {
            viewfinderView.drawViewfinder();
        }
    }

