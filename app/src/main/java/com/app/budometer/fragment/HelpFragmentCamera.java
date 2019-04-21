package com.app.budometer.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.budometer.R;
import com.app.budometer.util.HtmlBuilder;


/**
 * <p>Build a valid HTML string for a { TextView}.</p>
 *
 * <p>Example usage:</p>
 *
 * <pre>
 * <code>
 * HtmlBuilder html = new HtmlBuilder()
 * .p()
 * .b()
 * .font().color("red").face("sans-serif-condensed").text("Hello").close()
 * .close()
 * .close();
 *
 * // html.toString():
 * // &lt;p&gt;&lt;b&gt;&lt;font color=&quot;red&quot; face=&quot;sans-serif-condensed&quot;&gt;Hello&lt;/font&gt;&lt;/b&gt;&lt;/p&gt;
 *
 * yourEditText.setText(html.toSpan());
 * </code>
 * </pre>
 *
 * <p>HTML Tags Supported by  TextView:</p>
 *
 * <ul>
 * <li><code>&lt;a href=&quot;...&quot;&gt;</code></li>
 * <li><code>&lt;b&gt;</code></li>
 * <li><code>&lt;big&gt;</code></li>
 * <li><code>&lt;blockquote&gt;</code></li>
 * <li><code>&lt;br&gt;</code></li>
 * <li><code>&lt;cite&gt;</code></li>
 * <li><code>&lt;dfn&gt;</code></li>
 * <li><code>&lt;div align=&quot;...&quot;&gt;</code></li>
 * <li><code>&lt;em&gt;</code></li>
 * <li><code>&lt;font color=&quot;...&quot; face=&quot;...&quot;&gt;</code></li>
 * <li><code>&lt;h1&gt;</code></li>
 * <li><code>&lt;h2&gt;</code></li>
 * <li><code>&lt;h3&gt;</code></li>
 * <li><code>&lt;h4&gt;</code></li>
 * <li><code>&lt;h5&gt;</code></li>
 * <li><code>&lt;h6&gt;</code></li>
 * <li><code>&lt;i&gt;</code></li>
 * <li><code>&lt;img src=&quot;...&quot;&gt;</code></li>
 * <li><code>&lt;p&gt;</code></li>
 * <li><code>&lt;small&gt;</code></li>
 * <li><code>&lt;strike&gt;</code></li>
 * <li><code>&lt;strong&gt;</code></li>
 * <li><code>&lt;sub&gt;</code></li>
 * <li><code>&lt;sup&gt;</code></li>
 * <li><code>&lt;tt&gt;</code></li>
 * <li><code>&lt;u&gt;</code></li>
 * <li><code>&lt;ul&gt;</code> (Android 7.0+)</li>
 * <li><code>&lt;li&gt;</code> (Android 7.0+)</li>
 * </ul>
 */
public class HelpFragmentCamera extends BaseFragment {
    public static final String TAG = HelpFragmentCamera.class.getSimpleName();
    private static HelpFragmentCamera fragment = null;
    private OnHelpCameraFragmentInteractionListener mListener;
    private HtmlBuilder html;
    private TextView helpDescriptionTextViewMain;
    private TextView helpDescriptionTextViewGroup;
    private TextView cameraDescriptionTextView;
    private TextView cameraCaptureTextView0;
    private TextView cameraPreviewTextView1;

    public interface OnHelpCameraFragmentInteractionListener {
        void onHelpCameraFragmentInteraction();
    }

    public HelpFragmentCamera() {
    }

    public static HelpFragmentCamera newInstance() {
        if (fragment == null)
            fragment = new HelpFragmentCamera();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHelpCameraFragmentInteractionListener) {
            mListener = (OnHelpCameraFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHelpCameraFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help_camera, container, false);

        html = new HtmlBuilder();
        helpDescriptionTextViewMain = view.findViewById(R.id.helpDescriptionTextViewMain);
        html.p(getString(R.string.help_section_description_expand_button)).close();
        helpDescriptionTextViewMain.setText(html.build());

        html = new HtmlBuilder();
        helpDescriptionTextViewGroup = view.findViewById(R.id.helpDescriptionTextViewGroup);
        html.p().font().color("white")
                .text(getString(R.string.help_section_description_navigation_menu)).close();
        helpDescriptionTextViewGroup.setText(html.build());

        html = new HtmlBuilder();
        cameraDescriptionTextView = view.findViewById(R.id.cameraDescriptionTextView);
        html.p(getString(R.string.help_section_description_camera_button_1))
                .p().font().color("red")
                .text(getString(R.string.help_section_description_camera_button_2)).close();
        cameraDescriptionTextView.setText(html.build());

        html = new HtmlBuilder();
        cameraCaptureTextView0 = view.findViewById(R.id.cameraCaptureTextView0);
        html.p().font().color("red")
                .text(getString(R.string.help_description_camera_placement_photo_capture_2)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_start_the_camera)).close()
                .li(getString(R.string.help_description_camera_placement_photo_capture_1)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_camera_front_rear)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_click_capture)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_camera_flash_setting)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_camera_retake)).close()
                .li(getString(R.string.help_description_camera_save_image_1))
                .p().font().color("red")
                .text(getString(R.string.help_description_camera_save_image_2)).close();
        cameraCaptureTextView0.setText(html.close().build());

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

