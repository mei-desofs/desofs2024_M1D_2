import { IPaymentMySuffix } from 'app/shared/model/payment-my-suffix.model';

export interface ICartMySuffix {
  id?: number;
  total?: number | null;
  payment?: IPaymentMySuffix | null;
}

export const defaultValue: Readonly<ICartMySuffix> = {};
