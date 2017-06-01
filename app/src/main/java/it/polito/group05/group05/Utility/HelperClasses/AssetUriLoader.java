package it.polito.group05.group05.Utility.HelperClasses;

import android.content.Context;
import android.net.Uri;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

import java.io.InputStream;

/**
 * A base ModelLoader for {@link android.net.Uri}s that handles assets {@link android.net.Uri}s directly.
 */
public class AssetUriLoader implements StreamModelLoader<Uri> {
    private final Context context;

    public AssetUriLoader(Context context) {
        this.context = context;
    }

    @Override
    public final DataFetcher<InputStream> getResourceFetcher(Uri model, int width, int height) {
        AssetUriFetcher result = new AssetUriFetcher(context, model);
        return result;
    }
}