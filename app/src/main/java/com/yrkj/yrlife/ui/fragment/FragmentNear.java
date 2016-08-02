package com.yrkj.yrlife.ui.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.yrkj.yrlife.R;

import com.yrkj.yrlife.app.AppException;
import com.yrkj.yrlife.app.AppManager;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.Washing_no_card_record;
import com.yrkj.yrlife.ui.LoginActivity;
import com.yrkj.yrlife.ui.NearActivity;
import com.yrkj.yrlife.ui.WashActivity;
import com.yrkj.yrlife.ui.WashNActivity;
import com.yrkj.yrlife.ui.WashNnActivity;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;
import com.zxing.activity.CaptureActivity;
import com.zxing.camera.CameraManager;
import com.zxing.decoding.CaptureActivityHandler;
import com.zxing.decoding.InactivityTimer;
import com.zxing.view.ViewfinderView;


import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import com.zxing.decoding.DecodeHandlerInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.XMLFormatter;

/**
 * Created by Administrator on 2016/3/17.
 */
@ContentView(R.layout.fragment_near)
public class FragmentNear extends BaseFragment implements Callback, DecodeHandlerInterface {
    private CaptureActivityHandler handler;
    @ViewInject(R.id.viewfinder_view)
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    public static final int MY_PERMISSIONS_CAMERA = 1;
    private boolean vibrate;
    View view;
    LinearLayout ls;
    Camera camera = null;
    boolean isCamera = false;

    @ViewInject(R.id.title)
    private TextView title;
    ProgressDialog mLoadingDialog;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;
        CameraManager.init(getActivity());
        view.findViewById(R.id.back).setVisibility(View.INVISIBLE);
        title.setText("无卡洗车");
        ls = (LinearLayout) getActivity().findViewById(R.id.ssss);
        ls.setVisibility(View.GONE);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(getActivity());
        mLoadingDialog = UIHelper.progressDialog(getActivity(), "正在验证洗车机编号...");
    }

    @Event(R.id.kd_rl)
    private void kdrlEvent(View view) {
        CameraManager.get().flashHandler();

    }

    @Event(R.id.shuru_rl)
    private void shururlEvent(View view) {
        Intent intent = new Intent(getActivity(), WashNActivity.class);
        startActivity(intent);
    }

    @Event(R.id.near_rl)
    private void nearrlEvent(View view) {
        Intent intent = new Intent(getActivity(), NearActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) view.findViewById(R.id.preview_view);
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
        AudioManager audioService = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    public void onDestroy() {
        if (inactivityTimer != null) {
            inactivityTimer.shutdown();
            super.onDestroy();
        }
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(com.google.zxing.Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        //FIXME
        if (resultString.equals("")) {
            Toast.makeText(getContext(), "二维码中没有正确信息", Toast.LENGTH_SHORT).show();
        } else {
            if (resultString.length() == 6) {
                if (StringUtils.isNumber(resultString)) {
                    mLoadingDialog.show();
                    seach_machid(resultString);
                } else {
                    UIHelper.ToastMessage(appContext, "机器编号不正确！");
                }
            } else {
                UIHelper.ToastMessage(appContext, "机器编号不正确！");
            }

        }
    }

    private void seach_machid(final String mach_id) {
        RequestParams params = new RequestParams(URLs.Seach_Machid);
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        params.addQueryStringParameter("machineNum", mach_id);
        x.http().get(params, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                Result result = JsonUtils.fromJson(string, Result.class);
                mLoadingDialog.dismiss();
                if (result.OK()) {
                    Intent resultIntent = new Intent(getActivity(), WashNnActivity.class);
                    Bundle bundle = new Bundle();
//                    bundle.putString("result", resultString);
                    bundle.putString("wash_nub", mach_id);
                    resultIntent.putExtras(bundle);
//            getActivity().setResult(getActivity().RESULT_OK, resultIntent);
                    startActivity(resultIntent);
                } else if (result.isOK()){
                    UIHelper.CenterToastMessage(appContext, result.Message());
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    UIHelper.CenterToastMessage(appContext, result.Message());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIHelper.ToastMessage(appContext,"取消");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(appContext,ex.getMessage());
            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    /**
     * you should get result like this.
     * <p/>
     * String scanResult = data.getExtras().getString("result");
     */
    @Override
    public void resturnScanResult(int resultCode, Intent data) {

//		Toast.makeText(getActivity(), data.getExtras().getString("result"), 0)
//				.show();
    }

    @Override
    public void launchProductQuary(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivity(intent);
    }
}
