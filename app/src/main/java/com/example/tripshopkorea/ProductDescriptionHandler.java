package com.example.tripshopkorea;

import com.example.tripshopkorea.databinding.ActSecondBinding;

public class ProductDescriptionHandler implements ProductDescription.ProductDescriptionCallback {
    private ActSecondBinding binding;

    public ProductDescriptionHandler(ActSecondBinding binding) {
        this.binding = binding;
    }

    @Override
    public void onSuccess(String description) {
        binding.tvDescription.setText(description);
    }

    @Override
    public void onFailure(Exception e) {
        // Handle the error
    }
}