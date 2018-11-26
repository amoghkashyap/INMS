import * as protobuf from "protobufjs";
import { Message, Type, Field } from "protobufjs/light";

export enum StatusCode {
    UNSPECIFIED = 0,
    SUCCESS = 1,
    PARTIAL_SUCCESS = 2,
    FAILURE = 3
}

export enum DescriptionCode {
    NOT_SPECIFIED = 0,
    INVALID_ARGUMENT = 1,
    SERVICE_UNAVAILABLE = 2
}

@Type.d("StatusDescription")
class StatusDescription extends Message<Status> {

    @Field.d(1, DescriptionCode)
    public description_code: string;

    @Field.d(2, "string")
    public description: string;

}

@Type.d("Status")
class Status extends Message<Status> {

    @Field.d(1, StatusCode)
    public status_code: string;

    @Field.d(2, StatusDescription)
    public status_description: StatusDescription;

}

export class ResultCode {
    public static createStatusMessage(status_code: any, description_code: any, description: string) {
        return {"status": Status.create({
            status_code: status_code,
            status_description: {
                description_code: description_code,
                description: description
            }
        })};
    }
}
