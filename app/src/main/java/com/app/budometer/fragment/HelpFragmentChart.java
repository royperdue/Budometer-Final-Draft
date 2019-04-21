package com.app.budometer.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
 * <p>HTML Tags Supported by { TextView}:</p>
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
public class HelpFragmentChart extends BaseFragment {
    public static final String TAG = HelpFragmentChart.class.getSimpleName();
    private static HelpFragmentChart fragment = null;
    private OnHelpChartFragmentInteractionListener mListener;

    private HtmlBuilder html;
    private TextView chartDescriptionTextView;

    private TextView chartTextView;

    public interface OnHelpChartFragmentInteractionListener {
        void onHelpChartFragmentInteraction();
    }

    public HelpFragmentChart() {
    }

    public static HelpFragmentChart newInstance() {
        if (fragment == null)
            fragment = new HelpFragmentChart();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHelpChartFragmentInteractionListener) {
            mListener = (OnHelpChartFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHelpChartFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help_chart, container, false);

        html = new HtmlBuilder();
        chartDescriptionTextView = view.findViewById(R.id.chartDescriptionTextView);
        html.p(getString(R.string.help_description_chart_main)).close();
        chartDescriptionTextView.setText(html.build());

        html = new HtmlBuilder();
        chartTextView = view.findViewById(R.id.chartTextView);
        html.li().font().color("white")
                .text(getString(R.string.help_description_chart_four_images_used)).close()
                .li(getString(R.string.help_description_chart_pie_chart)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_chart_pie_chart_percentage_values)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_chart_color_bar_1)).close()
                .p().i(getString(R.string.help_description_chart_color_bar_2)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_chart_close_button)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_chart_save_button)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_chart_result_text_1)).close()
                .p().font().color("white")
                .text(getString(R.string.help_description_chart_result_text_2)).close()
                .li(getString(R.string.help_description_chart_pistil_percent)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_chart_strain_1)).close()
                .p().font().color("white")
                .text(getString(R.string.help_description_chart_strain_2)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_chart_crop_id_1)).close()
                .p().font().color("white")
                .text(getString(R.string.help_description_chart_crop_id_2)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_chart_date_1)).close()
                .p().font().color("white")
                .text(getString(R.string.help_description_chart_date_2)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_chart_notes)).close();
        chartTextView.setText(html.close().build());

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

