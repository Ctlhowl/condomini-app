import { Apartment } from "./apartment";
import { Outlay } from "./outlay";
import { Quote } from "./quote";
import { Report } from "./report";

export interface Condominium {
    id: number;
    name: string;
    address: string;
    lastYearBalance: number;
    
    outlays?: Outlay[];
    apartments?: Apartment[];
    reports?: Report[];
    quotes?: Quote[];
}