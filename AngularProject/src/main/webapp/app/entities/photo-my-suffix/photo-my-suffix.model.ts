import dayjs from 'dayjs/esm';
import { IPortfolioMySuffix } from 'app/entities/portfolio-my-suffix/portfolio-my-suffix.model';
import { ICartMySuffix } from 'app/entities/cart-my-suffix/cart-my-suffix.model';
import { PhotoState } from 'app/entities/enumerations/photo-state.model';

export interface IPhotoMySuffix {
  id: number;
  photo?: string | null;
  photoContentType?: string | null;
  date?: dayjs.Dayjs | null;
  state?: keyof typeof PhotoState | null;
  portfolio?: Pick<IPortfolioMySuffix, 'id'> | null;
  cart?: Pick<ICartMySuffix, 'id'> | null;
}

export type NewPhotoMySuffix = Omit<IPhotoMySuffix, 'id'> & { id: null };
