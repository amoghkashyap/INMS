
export class HTTP {
    static OK: number = 200;
    static ERROR: number = 500;
}

export default class HttpGrpcMap {

    static SUCCESS: number = 200; // 200 OK
    static UNKNOWN: number = 500; // 500 Internal Server Error
    static SERVICE_UNAVAILABLE: number = 503; // 503 Service unavailable  

    private static instance: HttpGrpcMap;

    static getInstance(): HttpGrpcMap {
        if (this.instance === null || this.instance === undefined) {
            this.instance = new HttpGrpcMap();
        }
        return this.instance;
    }
    public grpcCodeToHttpStatus(grpcStatusCode): number { 
        switch (grpcStatusCode) {
            case 0: // OK(0)
                return HttpGrpcMap.SUCCESS;
            case 1: // UNAVAILABLE(1)
                return HttpGrpcMap.SERVICE_UNAVAILABLE;
            // GRPC-HTTP Mapping
            case "SUCCESS": {
                return HttpGrpcMap.SUCCESS;
            }
            case "FAIL": {
                return HttpGrpcMap.SUCCESS;
            }
            case "NOT_FOUND": {
                return HttpGrpcMap.SUCCESS;
            }
            default: return HttpGrpcMap.UNKNOWN;
        }
    }
}