import Logger from "../config/logger";
import Path = require("path");
import { ResultCode, DescriptionCode, StatusCode } from "../utils/app.utils.resultcode.handler";
import ResponseHandler from "../utils/app.utils.response.handler";

let grpc = require("grpc");
let ServiceConstants;
ServiceConstants = require("../config/constants");

let host = ServiceConstants.ServiceHostName.INMS_SERVICE;
let port = ServiceConstants.ServicePort.INMS_SERVICE;

Logger.Instance.info("INMS_ORCHESTRATOR:", host, ":", port);

let proto = grpc.load({ "root": Path.join(__dirname, "/../../../NutritionManagementSystem"), "file": "/inms.proto" });
let client = new proto.inms.BackendRequests(host + ":" + port, grpc.credentials.createInsecure());

let client1 = new proto.inms.Detection(host + ":" + port, grpc.credentials.createInsecure());

export default class INMSService {
    private static instance: INMSService;

    static getInstance(): INMSService {
        if (this.instance === null || this.instance === undefined) {
            this.instance = new INMSService();
        }

        return this.instance;
    }

    public getIngredients(getIngredientsRequest, callback: Function): void {
        Logger.Instance.info("INMS_ORCHESTRATOR: " + "GetIngredients grpc service");
        try {
            client.getIngredients(getIngredientsRequest, (err, data) => ResponseHandler.getInstance().sendGrpcResponse(err, data, callback));
    
        }
        catch (error) {
            Logger.Instance.error("INMS_ORCHESTRATOR: " + "getIngredients: " + error);
            ResponseHandler.getInstance().dataTypeValidationResponse(error, callback);
        }
    }

    public getRecipes(getRecipesRequest, callback: Function): void {
        Logger.Instance.info("INMS_ORCHESTRATOR: " + "GetRecipes grpc service");
        try {
            client.getRecipes(getRecipesRequest, (err, data) => ResponseHandler.getInstance().sendGrpcResponse(err, data, callback));
        }
        catch (error) {
            Logger.Instance.error("INMS_ORCHESTRATOR: " + "getRecipes: " + error);
            ResponseHandler.getInstance().dataTypeValidationResponse(error, callback);
        }
    }

    public updateDetectedIngredients(detectedIngredientsRequest, callback: Function): void {
        Logger.Instance.info("INMS_ORCHESTRATOR: " + " UpdateDetectedIngredients grpc service");
        try {
            client1.updateDetectedIngredients(detectedIngredientsRequest, (err, data) => ResponseHandler.getInstance().sendGrpcResponse(err, data, callback));
        }
        catch (error) {
            Logger.Instance.error("INMS_ORCHESTRATOR: " + "updateDetectedIngredients: " + error);
            ResponseHandler.getInstance().dataTypeValidationResponse(error, callback);
        }
    }
}



