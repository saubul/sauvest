export class ChatModel {
    id: number
    usersUsername: Array<string>
    name: string

    constructor(usernames: Array<string>, name: string) {
        this.usersUsername = usernames;
        this.name = name;
    }
}