import { IPortfolioMySuffix } from 'app/shared/model/portfolio-my-suffix.model';

export interface IUserMySuffix {
  id?: number;
  email?: string | null;
  password?: string | null;
  address?: string | null;
  contact?: string | null;
  profilePhotoContentType?: string | null;
  profilePhoto?: string | null;
  portfolio?: IPortfolioMySuffix | null;
}

export const defaultValue: Readonly<IUserMySuffix> = {};
