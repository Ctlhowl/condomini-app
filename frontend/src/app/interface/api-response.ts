export interface ApiResponse<T>{
    timestamp: string;
    statusCode: number;
    httpStatus?: string;
    reason?: string;
    message: string;
    data: {[key: string]: T}
}