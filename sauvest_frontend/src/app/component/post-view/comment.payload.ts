export class CommentPayload {
    content: string;
    username: string;
    postId: number;
    dateCreated: Date;
    userImg?: string;
}