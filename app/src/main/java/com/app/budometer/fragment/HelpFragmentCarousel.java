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
public class HelpFragmentCarousel extends BaseFragment {
    private static HelpFragmentCarousel fragment = null;
    private OnHelpCarouselFragmentInteractionListener mListener;
    private HtmlBuilder html;
    private TextView reviewDescriptionTextView;

    private TextView reviewListTextView0;
    private TextView reviewListTextView1;

    public interface OnHelpCarouselFragmentInteractionListener {
        void onHelpCarouselFragmentInteraction();
    }

    public HelpFragmentCarousel() {
    }

    public static HelpFragmentCarousel newInstance() {
        if (fragment == null)
            fragment = new HelpFragmentCarousel();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHelpCarouselFragmentInteractionListener) {
            mListener = (OnHelpCarouselFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHelpCarouselFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help_carousel, container, false);

        html = new HtmlBuilder();
        reviewDescriptionTextView = view.findViewById(R.id.reviewDescriptionTextView);
        html.p(getString(R.string.help_description_carousel_main)).close();
        reviewDescriptionTextView.setText(html.build());

        html = new HtmlBuilder();
        reviewListTextView0 = view.findViewById(R.id.reviewListTextView0);
        html.p().font().color("white")
                .text(getString(R.string.help_description_vertical_scroll)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_carousel_list_header_1)).close()
                .p().font().color("white")
                .text(getString(R.string.help_description_carousel_list_header_2)).close()
                .p().font().color("white")
                .text(getString(R.string.help_description_carousel_list_header_3)).close()
                .p().font().color("white")
                .text(getString(R.string.help_description_carousel_list_header_4)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_carousel_click_list_elements_1)).close()
                .p().font().color("white")
                .text(getString(R.string.help_description_carousel_click_list_elements_2)).close()
                .p().font().color("white")
                .text(getString(R.string.help_description_carousel_display_archived_information_1)).close()
                .p().font().color("white")
                .text(getString(R.string.help_description_carousel_display_archived_information_2)).close();
        reviewListTextView0.setText(html.close().build());

        html = new HtmlBuilder();
        reviewListTextView1 = view.findViewById(R.id.reviewListTextView1);
        html.p(getString(R.string.help_description_carousel_horizontal_scroll)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_chart_four_images_used)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_chart_pie_chart)).close()
                .li(getString(R.string.help_description_chart_pie_chart_percentage_values)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_chart_color_bar_1)).close()
                .p().i(getString(R.string.help_description_chart_color_bar_2)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_carousel_delete_button)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_carousel_close_button)).close()
                .li().font().color("white")
                .text(getString(R.string.help_description_carousel_input_displayed)).close();
        reviewListTextView1.setText(html.close().build());

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

