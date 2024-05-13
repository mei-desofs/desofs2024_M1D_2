import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPortfolioMySuffix, NewPortfolioMySuffix } from '../portfolio-my-suffix.model';

export type PartialUpdatePortfolioMySuffix = Partial<IPortfolioMySuffix> & Pick<IPortfolioMySuffix, 'id'>;

type RestOf<T extends IPortfolioMySuffix | NewPortfolioMySuffix> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestPortfolioMySuffix = RestOf<IPortfolioMySuffix>;

export type NewRestPortfolioMySuffix = RestOf<NewPortfolioMySuffix>;

export type PartialUpdateRestPortfolioMySuffix = RestOf<PartialUpdatePortfolioMySuffix>;

export type EntityResponseType = HttpResponse<IPortfolioMySuffix>;
export type EntityArrayResponseType = HttpResponse<IPortfolioMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class PortfolioMySuffixService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/portfolios');

  create(portfolio: NewPortfolioMySuffix): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(portfolio);
    return this.http
      .post<RestPortfolioMySuffix>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(portfolio: IPortfolioMySuffix): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(portfolio);
    return this.http
      .put<RestPortfolioMySuffix>(`${this.resourceUrl}/${this.getPortfolioMySuffixIdentifier(portfolio)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(portfolio: PartialUpdatePortfolioMySuffix): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(portfolio);
    return this.http
      .patch<RestPortfolioMySuffix>(`${this.resourceUrl}/${this.getPortfolioMySuffixIdentifier(portfolio)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPortfolioMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPortfolioMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPortfolioMySuffixIdentifier(portfolio: Pick<IPortfolioMySuffix, 'id'>): number {
    return portfolio.id;
  }

  comparePortfolioMySuffix(o1: Pick<IPortfolioMySuffix, 'id'> | null, o2: Pick<IPortfolioMySuffix, 'id'> | null): boolean {
    return o1 && o2 ? this.getPortfolioMySuffixIdentifier(o1) === this.getPortfolioMySuffixIdentifier(o2) : o1 === o2;
  }

  addPortfolioMySuffixToCollectionIfMissing<Type extends Pick<IPortfolioMySuffix, 'id'>>(
    portfolioCollection: Type[],
    ...portfoliosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const portfolios: Type[] = portfoliosToCheck.filter(isPresent);
    if (portfolios.length > 0) {
      const portfolioCollectionIdentifiers = portfolioCollection.map(portfolioItem => this.getPortfolioMySuffixIdentifier(portfolioItem));
      const portfoliosToAdd = portfolios.filter(portfolioItem => {
        const portfolioIdentifier = this.getPortfolioMySuffixIdentifier(portfolioItem);
        if (portfolioCollectionIdentifiers.includes(portfolioIdentifier)) {
          return false;
        }
        portfolioCollectionIdentifiers.push(portfolioIdentifier);
        return true;
      });
      return [...portfoliosToAdd, ...portfolioCollection];
    }
    return portfolioCollection;
  }

  protected convertDateFromClient<T extends IPortfolioMySuffix | NewPortfolioMySuffix | PartialUpdatePortfolioMySuffix>(
    portfolio: T,
  ): RestOf<T> {
    return {
      ...portfolio,
      date: portfolio.date?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPortfolioMySuffix: RestPortfolioMySuffix): IPortfolioMySuffix {
    return {
      ...restPortfolioMySuffix,
      date: restPortfolioMySuffix.date ? dayjs(restPortfolioMySuffix.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPortfolioMySuffix>): HttpResponse<IPortfolioMySuffix> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPortfolioMySuffix[]>): HttpResponse<IPortfolioMySuffix[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
