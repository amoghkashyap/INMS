import * as http from "http";
import * as express from "express";
import config from "./config/express";
import ApplicationRouter from "./routes/application.route";
import Logger from "./config/logger";
let Constants = require("./config/constants");

let hostname = Constants.ServiceHostName.INMS_SDK;
let defaultPort = Constants.ServicePort.INMS_SDK;

let grpc = require("grpc");
let swaggerUi = require("swagger-ui-express");
const swaggerJSDoc = require("swagger-jsdoc");

const spec = swaggerJSDoc({
    "swaggerDefinition": {
        "info": {
            "title": "INMS",
            "version": "1.0.0"
        },
        "produces": ["application/json", "text/plain"],

        "consumes": ["application/json"],
        "host": process.env.routeurl,
        "basePath": "/api-internal/v2",
        // Authorisation is in HTTP Header
        "securityDefinitions": {
            "jwt": {
                "type": "apiKey",
                "in": "header",
                "name": "Authorization"
            }
        }
    },
    "apis": [
        "./src/routes/inms.route.ts"        
    ]  
});

export default class Application {
    app: express.Express;
    server: http.Server;
    port: string;

    constructor() {
        this.app = config(express());
        this.server = http.createServer(this.app);
        this.routes();
        this.start(this.server);
    }

    public static bootstrap(): Application {
        return new Application();
    }

    private routes(): void {
        let router: express.Router = express.Router({ caseSensitive: true });
        ApplicationRouter.create(router);
        this.app.use("/api-internal/INMS/Api_Docs", swaggerUi.serve, swaggerUi.setup(spec));
        this.app.use(router);
    }

    public start(server) {
        this.port = defaultPort;
        server.listen(this.port, "0.0.0.0");
        server.on("error", (e: Error) => {
		Logger.Instance.error("INMS_ORCHESTRATOR: " + "error starting server!", e.message);
        });
        server.on("listening", () => {
            Logger.Instance.info("INMS_ORCHESTRATOR: " + "Server started on http://" + hostname + ":" + this.port);
        });
    }
}

let app = new Application();
