export class TokenResponsePayload {

    accessToken: string;
    refreshToken: string;
    username: string;

    constructor(accessToken:string, refreshToken:string, username: string) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
    }
}