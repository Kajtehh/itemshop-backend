package pl.kajteh.itemshop.model.order;

import java.util.UUID;

public record OrderRequest(String nickname, String email, String paymentChannel, UUID serverId, UUID productId, UUID variantId, int quantity) {}
