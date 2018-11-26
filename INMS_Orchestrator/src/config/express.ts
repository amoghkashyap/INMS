import * as bodyParser from "body-parser";
import * as express from "express";
import * as path from "path";

import ApplicationRouter from "../routes/application.route";
import { ResultCode, DescriptionCode, StatusCode } from "../utils/app.utils.resultcode.handler";
import { HTTP } from "./http.codes";
import Logger from "./logger";

let helmet = require("helmet");
let cors = require("cors");
let corsOptions = {
    "origin": "*",
    "methods": "GET,HEAD,PUT,PATCH,POST,DELETE,OPTIONS",
    "allowedHeaders": "Content-Type,Authorization"
};

export default function config(app: express.Express) {

    app.use(bodyParser.json());
    app.use(bodyParser.urlencoded({ extended: false }));
    app.use(express.static(path.join(__dirname, "../public")));

    // Set X-Xss-Protection, X-FrameOptions and X-Content Type Response Headers
    app.use(helmet());

    // Request CORS configuration
    app.use(cors(corsOptions));

    // error handler
    app.use((err: any, req: express.Request, res: express.Response, next): void => {
        let result = ResultCode.createStatusMessage(StatusCode.FAILURE, DescriptionCode.INVALID_ARGUMENT, err.message);
        res.status(err.status).send(result);
    });

    return app;
}