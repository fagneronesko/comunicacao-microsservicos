import Order from "../modules/sales/model/Order.js";

export async function createdInitialData() {
    await Order.collection.drop();

    await Order.create({
        products: [
            { productId: 1001, quantity: 2 },
            { productId: 1002, quantity: 2 },
            { productId: 1003, quantity: 1 },
        ],
        user: {
            id: "asdaklçsdjfasdf",
            name: "admin",
            email: "admin@admin.com",
        },
        status: "APPROVED",
        createdAt: new Date(),
        updatedAt: new Date(),
    });

    await Order.create({
        products: [
            { productId: 1001, quantity: 4 },
            { productId: 1003, quantity: 3 },
        ],
        user: {
            id: "kldçfjklaçsfasf",
            name: "admin2",
            email: "admin2@admin.com",
        },
        status: "REJECTED",
        createdAt: new Date(),
        updatedAt: new Date(),
    });
}
