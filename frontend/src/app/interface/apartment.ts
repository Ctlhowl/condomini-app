import { Outlay } from "./outlay";

export interface Apartment {
    id: number;
    owner: string;
    tenant: string;
    millTabA: number;
    millTabB: number;
    millTabC: number;
    millTabD: number;
    scala: string;
    lastYearBalance: number;

    outlays: Outlay[];
}