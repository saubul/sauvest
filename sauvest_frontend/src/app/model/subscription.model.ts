export class SubscriptionModel {
    username: string
    subUsername: string

    constructor(username: string, subUsername: string) {
        this.username = username;
        this.subUsername = subUsername;
    }
}