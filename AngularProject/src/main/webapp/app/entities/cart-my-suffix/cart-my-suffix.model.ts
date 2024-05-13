import { IPaymentMySuffix } from 'app/entities/payment-my-suffix/payment-my-suffix.model';

export interface ICartMySuffix {
  id: number;
  total?: number | null;
  payment?: Pick<IPaymentMySuffix, 'id'> | null;
}

export type NewCartMySuffix = Omit<ICartMySuffix, 'id'> & { id: null };
