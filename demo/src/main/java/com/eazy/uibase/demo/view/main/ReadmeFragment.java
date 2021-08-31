package com.eazy.uibase.demo.view.main;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.eazy.uibase.demo.R;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import io.noties.markwon.AbstractMarkwonPlugin;
import io.noties.markwon.Markwon;
import io.noties.markwon.MarkwonConfiguration;
import io.noties.markwon.ext.tables.TablePlugin;
import io.noties.markwon.ext.tasklist.TaskListPlugin;
import io.noties.markwon.html.HtmlPlugin;
import io.noties.markwon.image.AsyncDrawable;
import io.noties.markwon.image.AsyncDrawableLoader;
import io.noties.markwon.image.DrawableUtils;
import io.noties.markwon.image.ImagesPlugin;
import io.noties.markwon.recycler.table.TableEntryPlugin;

public class ReadmeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.readme_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = requireContext();
        Markwon markwon = Markwon.builder(context)
                .usePlugin(ImagesPlugin.create())
                .usePlugin(HtmlPlugin.create())
                .usePlugin(TablePlugin.create(requireContext()))
                .usePlugin(TaskListPlugin.create(requireContext()))
                .usePlugin(TableEntryPlugin.create(requireContext()))
                .usePlugin(new AbstractMarkwonPlugin() {
                    @Override
                    public void configureConfiguration(@NonNull @NotNull MarkwonConfiguration.Builder builder) {
                        super.configureConfiguration(builder);
                        builder.asyncDrawableLoader(new AsyncDrawableLoader() {
                            @Override
                            public void load(@NonNull @NotNull AsyncDrawable drawable) {
                                try (InputStream is = requireContext().getAssets().open(drawable.getDestination())) {
                                    Drawable drawable1 = Drawable.createFromStream(is, drawable.getDestination());
                                    final Rect bounds = drawable1.getBounds();
                                    if (bounds == null
                                            || bounds.isEmpty()) {
                                        DrawableUtils.applyIntrinsicBounds(drawable1);
                                    }
                                    drawable.setResult(drawable1);
                                } catch (Exception e) {
                                }
                            }
                            @Override
                            public void cancel(@NonNull @NotNull AsyncDrawable drawable) {
                            }
                            @Nullable
                            @org.jetbrains.annotations.Nullable
                            @Override
                            public Drawable placeholder(@NonNull @NotNull AsyncDrawable drawable) {
                                return null;
                            }
                        });
                    }
                })
                .build();
        try {
            String md = getReadmeContent();
            markwon.setMarkdown(view.findViewById(R.id.textView), md);
        } catch (IOException e) {
            markwon.setMarkdown(view.findViewById(R.id.textView), e.toString());
        }
    }

    private String getReadmeContent() throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStream is;
        try {
            is = requireContext().getAssets().open(getArguments().getString("file"));
        } catch (IOException e) {
            is = requireContext().getAssets().open(getArguments().getString("file2"));
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8 ));
        String str;
        while ((str = br.readLine()) != null) {
            sb.append(str);
            sb.append("\n");
        }
        br.close();
        return sb.toString();
    }
}
