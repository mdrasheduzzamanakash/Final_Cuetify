package com.example.final_cuetify.utilities;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpacingItem extends RecyclerView.ItemDecoration {
    private final int verticalSpacingHeight;

    public SpacingItem(int verticalSpacingHeight) {
        this.verticalSpacingHeight = verticalSpacingHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = verticalSpacingHeight;
        outRect.left = 5;
        outRect.right = 5;
    }
}
