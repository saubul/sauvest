import { InstrumentModel } from "./instrument.model";

export class PostModel {
    id: number;
    username: string;
    content: string;
    creationDateTime: Date;
    imgPath: string;
    voteCount: number;
    userImg?: string;
    isLikedByCurrentUser?: boolean;
    name: string;
    surname: string;
    instruments: Array<InstrumentModel>
}