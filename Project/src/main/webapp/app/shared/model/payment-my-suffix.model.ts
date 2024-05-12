import dayjs from 'dayjs';
import { IReceiptMySuffix } from 'app/shared/model/receipt-my-suffix.model';

export interface IPaymentMySuffix {
  id?: number;
  idCart?: number | null;
  date?: dayjs.Dayjs | null;
  receipt?: IReceiptMySuffix | null;
}

export const defaultValue: Readonly<IPaymentMySuffix> = {};
