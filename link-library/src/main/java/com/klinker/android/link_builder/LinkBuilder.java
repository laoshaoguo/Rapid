/*
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package com.klinker.android.link_builder;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.MovementMethod;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LinkBuilder {

    private static final int TYPE_TEXT = 1;
    private static final int TYPE_TEXT_VIEW = 2;

    public static LinkBuilder from(Context context, String text) {
        return new LinkBuilder(TYPE_TEXT)
                .setContext(context)
                .setText(text);
    }

    public static LinkBuilder on(TextView tv) {
        return new LinkBuilder(TYPE_TEXT_VIEW)
                .setContext(tv.getContext())
                .setTextView(tv);
    }

    private static final String TAG = "LinkBuilder";

    private int type;

    private Context context;

    private TextView textView;
    private CharSequence text;

    private boolean findOnlyFirstMatch = false;

    private List<Link> links = new ArrayList<>();

    private SpannableString spannable = null;

    /**
     * Construct a LinkBuilder object.
     *
     * @param type TYPE_TEXT or TYPE_TEXT_VIEW
     */
    private LinkBuilder(int type) {
        this.type = type;
    }

    @Deprecated
    public LinkBuilder(TextView textView) {
        if (textView == null) {
            throw new IllegalArgumentException("textView is null");
        }

        this.textView = textView;
        setText(textView.getText().toString());
    }

    public LinkBuilder setTextView(TextView textView) {
        this.textView = textView;
        return setText(textView.getText());
    }

    public LinkBuilder setText(CharSequence text) {
        this.text = text;
        return this;
    }

    public LinkBuilder setContext(Context context) {
        this.context = context;
        return this;
    }

    public LinkBuilder setFindOnlyFirstMatchesForAnyLink(boolean findOnlyFirst) {
        this.findOnlyFirstMatch = findOnlyFirst;
        return this;
    }

    /**
     * Add a single link to the builder.
     *
     * @param link the rule that you want to link with.
     */
    public LinkBuilder addLink(Link link) {
        if (link == null) {
            throw new IllegalArgumentException("link is null");
        }
        this.links.add(link);
        return this;
    }

    /**
     * Add a list of links to the builder.
     *
     * @param links list of rules you want to link with.
     */
    public LinkBuilder addLinks(List<Link> links) {
        if (links == null) {
            throw new IllegalArgumentException("link list is null");
        }
        if (links.isEmpty()) {
            throw new IllegalArgumentException("link list is empty");
        }
        for (Link link : links) {
            if (link == null) {
                throw new IllegalArgumentException("At least one link is null");
            }
        }
        this.links.addAll(links);
        return this;
    }

    /**
     * Execute the rules to create the linked text.
     */
    public SpannableString build() {
        // we extract individual links from the patterns
        turnPatternsToLinks();

        // exit if there are no links
        if (links.size() == 0) {
            return null;
        }

        // we need to apply this text before the links are created
        applyAppendedAndPrependedText();


        // add those links to our spannable text so they can be clicked
        for (Link link : links) {
            addLinkToSpan(link);
        }

        if (type == TYPE_TEXT_VIEW) {
            // set the spannable text
            textView.setText(spannable);

            // add the movement method so we know what actions to perform on the clicks
            addLinkMovementMethod();
        }

        return spannable;
    }

    /**
     * Add the link rule and check if spannable text is created.
     *
     * @param link rule to add to the text.
     */
    private void addLinkToSpan(Link link) {
        // create a new spannable string if none exists
        if (spannable == null) {
            spannable = SpannableString.valueOf(text);
        }

        // add the rule to the spannable string
        addLinkToSpan(spannable, link);
    }

    /**
     * Find the link within the spannable text
     *
     * @param s    spannable text that we are adding the rule to.
     * @param link rule to add to the text.
     */
    private void addLinkToSpan(Spannable s, Link link) {
        // get the current text
        Pattern pattern = Pattern.compile(Pattern.quote(link.getText()));
        Matcher matcher = pattern.matcher(text);
        NetUrlHandleBean netUrlHandleBean = null;
        if (link.getLinkMetadata() != null) {
            netUrlHandleBean = (NetUrlHandleBean) link.getLinkMetadata().getSeriObj(LinkMetadata.METADATA_KEY_COTENT);
        }
        int link_position = 0;
        int link_real_position = 0;
        // find one or more links inside the text
        while (matcher.find()) {

            // find the start and end point of the linked text within the TextView
            int start = matcher.start();

            //int start = text.indexOf(link.getText());
            if (start >= 0) {
                int end = start + link.getText().length();
                // add link to the spannable text
                if (link.getLinkMetadata() != null && link.getLinkMetadata().getSZObj(LinkMetadata.METADATA_KEY_TYPE) ==
                        LinkMetadata.SpanType.NET_SITE && netUrlHandleBean != null && netUrlHandleBean.getPositions().contains(link_position)) {
                    TouchableSpan span = applyLink(link, new Range(start, end), s);
                    if (span != null) {
                        span.position = link_real_position;
                    }
                    link_real_position++;
                } else if (!Link.DEFAULT_NET_SITE.equals(link.getText())) {
                    TouchableSpan spanNor = applyLink(link, new Range(start, end), s);
                    if (spanNor != null) {
                        spanNor.position = link_position;
                    }
                }
                link_position++;
            }

            // if we are only looking for the first occurrence of this pattern,
            // then quit now and don't look any further
            if (findOnlyFirstMatch) {
                break;
            }

        }
    }


    /**
     * Add the movement method to handle the clicks.
     */
    private void addLinkMovementMethod() {
        MovementMethod m = textView.getMovementMethod();

        if ((m == null) || !(m instanceof TouchableMovementMethod)) {
            if (textView.getLinksClickable()) {
                textView.setMovementMethod(TouchableMovementMethod.getInstance());
            }
        }
    }

    /**
     * Set the link rule to the spannable text.
     *
     * @param link  rule we are applying.
     * @param range the start and end point of the link within the text.
     * @param text  the spannable text to add the link to.
     */
    private TouchableSpan applyLink(Link link, Range range, Spannable text) {
        TouchableSpan[] existingSpans = text.getSpans(range.start, range.end, TouchableSpan.class);
        if (existingSpans.length == 0) {
            TouchableSpan span = new TouchableSpan(context, link);
            text.setSpan(span, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return span;
        } else {
            boolean newSpanConsumesAllOld = true;
            for (TouchableSpan span : existingSpans) {
                int start = spannable.getSpanStart(span);
                int end = spannable.getSpanEnd(span);
                if (range.start > start || range.end < end) {
                    newSpanConsumesAllOld = false;
                    break;
                } else {
                    text.removeSpan(span);
                }
            }

            if (newSpanConsumesAllOld) {
                TouchableSpan span = new TouchableSpan(context, link);
                text.setSpan(span, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return span;
            }
            return null;
        }
    }

    /**
     * Find the links that contain patterns and convert them to individual links.
     */
    private void turnPatternsToLinks() {
        int size = links.size();
        int i = 0;
        while (i < size) {
            if (links.get(i).getPattern() != null) {
                addLinksFromPattern(links.get(i));

                links.remove(i);
                size--;
            } else {
                i++;
            }
        }
    }

    /**
     * Add the appended and prepended text to the links and apply it to the TextView
     * so that we can create the SpannableString.
     */
    private void applyAppendedAndPrependedText() {
        for (int i = 0; i < links.size(); i++) {
            Link link = links.get(i);

            if (link.getPrependedText() != null) {
                String totalText = link.getPrependedText() + " " + link.getText();

                text = TextUtils.replace(text, new String[]{link.getText()}, new CharSequence[]{totalText});
                links.get(i).setText(totalText);
            }

            if (link.getAppendedText() != null) {
                String totalText = link.getText() + " " + link.getAppendedText();

                text = TextUtils.replace(text, new String[]{link.getText()}, new CharSequence[]{totalText});
                links.get(i).setText(totalText);
            }
        }
    }

    /**
     * Convert the pattern to individual links.
     *
     * @param linkWithPattern pattern we want to match.
     */
    private void addLinksFromPattern(Link linkWithPattern) {
        Pattern pattern = linkWithPattern.getPattern();
        Matcher m = pattern.matcher(text);
        int position = 0;
        while (m.find()) {
            links.add(new Link(linkWithPattern).setText(text.subSequence(m.start(), m.end()).toString()));

            // if we are only looking for the first occurrence of this pattern,
            // then quit now and don't look any further
            if (findOnlyFirstMatch) {
                break;
            }
            position++;
        }
    }

    /**
     * Manages the start and end points of the linked text.
     */
    public static class Range {
        public int start;
        public int end;

        public Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return "Range{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
    }
}
