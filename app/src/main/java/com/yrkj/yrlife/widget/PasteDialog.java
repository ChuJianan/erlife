package com.yrkj.yrlife.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yrkj.yrlife.R;

import org.xutils.view.annotation.ContentView;

/**
 * Created by cjn on 2016/10/10.
 */

public class PasteDialog extends Dialog {

    public PasteDialog(Context context) {
        super(context);
    }

    public PasteDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String sign_serial;
        private String sign_add;
        private String positiveButtonText;
        private String total_points;
        private View contentView;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String sign_add) {
            this.sign_add = sign_add;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param sign_add
         * @return
         */
        public Builder setMessage(int sign_add) {
            this.sign_add = (String) context.getText(sign_add);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param sign_serial
         * @return
         */
        public Builder setTitle(int sign_serial) {
            this.sign_serial = (String) context.getText(sign_serial);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param sign_serial
         * @return
         */

        public Builder setTitle(String sign_serial) {
            this.sign_serial = sign_serial;
            return this;
        }

        public Builder setTotal_points(int total_points) {
            this.total_points = (String) context.getText(total_points);
            return this;
        }

        /**
         * set the Dialog total_points from string
         *
         * @param total_points
         * @return
         */
        public Builder setTotal_points(String total_points) {
            this.total_points = total_points;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         DialogInterface.OnClickListener listener) {

            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonClickListener = listener;
            return this;
        }

        public PasteDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final PasteDialog dialog = new PasteDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.activity_past, null);
            dialog.addContentView(layout, new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.FILL_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ((TextView) layout.findViewById(R.id.sign_serial)).setText(sign_serial);
            //set the dialog total_points总积分
            ((TextView)layout.findViewById(R.id.text_jifen)).setText(total_points);
            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            // set the cancel button
//            if (negativeButtonText != null) {
//                ((Button) layout.findViewById(R.id.negativeButton))
//                        .setText(negativeButtonText);
//                if (negativeButtonClickListener != null) {
//                    ((Button) layout.findViewById(R.id.negativeButton))
//                            .setOnClickListener(new View.OnClickListener() {
//                                public void onClick(View v) {
//                                    negativeButtonClickListener.onClick(dialog,
//                                            DialogInterface.BUTTON_NEGATIVE);
//                                }
//                            });
//                }
//            } else {
//                // if no confirm button just set the visibility to GONE
//                layout.findViewById(R.id.negativeButton).setVisibility(
//                        View.GONE);
//            }
            // set the content message
            if (sign_add != null) {
                ((TextView) layout.findViewById(R.id.sign_add)).setText(sign_add);
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new RadioGroup.LayoutParams(RadioGroup.LayoutParams.FILL_PARENT, RadioGroup.LayoutParams.FILL_PARENT));
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
