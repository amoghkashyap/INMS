import { HTTP } from "../config/http.codes";
import { ResultCode, DescriptionCode, StatusCode } from "./app.utils.resultcode.handler";
import Logger from "../config/logger";

export default class ResponseHandler {
    private static instance: ResponseHandler;

    static getInstance(): ResponseHandler {
        if (this.instance === null || this.instance === undefined) {
            this.instance = new ResponseHandler();
        }
        return this.instance;
    }

    public sendHttpResponse(err: any, data: any, res: any) {
        if (err) {
            res.status(HTTP.OK).send(err);
            return;
        }
        res.status(HTTP.OK).send(data);
    }

    public sendGrpcResponse(err: any, data: any, callback: any) {
        if (err) {
            Logger.Instance.error("INMS_ORCHESTRATOR: GRPC communication failure:", err);
            let result = ResultCode.createStatusMessage(StatusCode.FAILURE, DescriptionCode.SERVICE_UNAVAILABLE, err.message);
            callback(result);
            return;
        }
        callback(null, data);
    }

    public dataTypeValidationResponse(err: any, callback: any) {
        let result = ResultCode.createStatusMessage(StatusCode.FAILURE, DescriptionCode.INVALID_ARGUMENT, err.message);
        callback(null, result);
    }
}