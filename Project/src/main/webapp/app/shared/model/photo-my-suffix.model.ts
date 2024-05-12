import dayjs from 'dayjs';
import { IPortfolioMySuffix } from 'app/shared/model/portfolio-my-suffix.model';
import { ICartMySuffix } from 'app/shared/model/cart-my-suffix.model';
import { PhotoState } from 'app/shared/model/enumerations/photo-state.model';

export interface IPhotoMySuffix {
  id?: number;
  photoContentType?: string | null;
  photo?: string | null;
  date?: dayjs.Dayjs | null;
  state?: keyof typeof PhotoState | null;
  portfolio?: IPortfolioMySuffix | null;
  cart?: ICartMySuffix | null;
}

export const defaultValue: Readonly<IPhotoMySuffix> = {};
