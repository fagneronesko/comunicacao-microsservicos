import express from "express";

import { connect } from "./src/config/db/MongoDbConfig.js";
import { createdInitialData } from "./src/config/db/InitalData.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8082;

connect();
createdInitialData();

app.get("/api/status", async (req, res) => {
    return res.status(200).json({
        service: "Sales-API",
        status: "up",
        httpStatus: 200,
    });
});

app.listen(PORT, () => {
    console.info(`Server started successfully at port ${PORT}`);
});
