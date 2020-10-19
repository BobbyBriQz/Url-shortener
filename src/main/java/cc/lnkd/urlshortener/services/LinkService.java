package cc.lnkd.urlshortener.services;

import cc.lnkd.urlshortener.exceptions.BadRequestException;
import cc.lnkd.urlshortener.models.request.LinkRequest;
import cc.lnkd.urlshortener.models.response.LinkResponse;

import java.sql.SQLException;

public interface LinkService {
    LinkResponse retrieveURLFromDB(String slug) throws SQLException;
    LinkResponse writeURLToDBForPaid(LinkRequest request) throws SQLException, BadRequestException;
    LinkResponse writeURLToDBForFree(LinkRequest request) throws SQLException, BadRequestException;
    LinkResponse writeURLToDBForIncognito(LinkRequest request) throws SQLException, BadRequestException;
}
