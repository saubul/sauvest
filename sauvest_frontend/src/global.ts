export const api_v1_url: string = '/api/v1'

export const auth_application_name = 'sauvest-auth'
export const auth_url: string = 'http://localhost:8080/' + auth_application_name + api_v1_url; 

export const social_application_name = 'sauvest-social'
export const social_application_host = 'http://localhost:8081/'
export const social_url: string = social_application_host + social_application_name + api_v1_url;

export const market_app_name = "sauvest-market";
export const market_app_host = "http://localhost:8082/";
export const market_url = market_app_host + market_app_name + api_v1_url;

export const imagesDirectory: string = '/assets/img/';
//export const server_url='http://195.133.49.174:33000'