import dayjs from 'dayjs/esm';
import { IReceiptMySuffix } from 'app/entities/receipt-my-suffix/receipt-my-suffix.model';

export interface IPaymentMySuffix {
  id: number;
  idCart?: number | null;
  date?: dayjs.Dayjs | null;
  receipt?: Pick<IReceiptMySuffix, 'id'> | null;
}

export type NewPaymentMySuffix = Omit<IPaymentMySuffix, 'id'> & { id: null };
