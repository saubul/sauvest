export class CommentModel {
    id: number
    postId: number
    username: string
    content: string
    creationDateTime: Date
    userImg?: string;
}